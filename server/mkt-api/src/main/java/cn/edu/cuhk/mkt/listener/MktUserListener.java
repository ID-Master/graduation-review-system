package cn.edu.cuhk.mkt.listener;

import cn.edu.cuhk.mkt.common.enums.UserEnum;
import cn.edu.cuhk.mkt.common.util.UserUtil;
import cn.edu.cuhk.mkt.entity.biz.CourseCategory;
import cn.edu.cuhk.mkt.entity.biz.CourseMaster;
import cn.edu.cuhk.mkt.entity.report.MktUserDto;
import cn.edu.cuhk.mkt.entity.sys.ProfilesVO;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.service.biz.CourseMasterService;
import cn.edu.cuhk.mkt.service.sys.ProfilesService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.support.excel.AbstractImportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MktUserListener extends AbstractImportListener<MktUserDto> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final CourseMasterService courseMasterService;
    private final ProfilesService profilesService;

    private List<User> insertForUserList = Lists.newArrayList();
    private List<CourseMaster> insertForCourseMasterList = Lists.newArrayList();
    private List<String> studentList = Lists.newArrayList();

    public MktUserListener(UserService userService, CourseMasterService courseMasterService, ProfilesService profilesService) {
        this.userService = userService;
        this.courseMasterService = courseMasterService;
        this.profilesService = profilesService;
    }

    @Override
    protected void doFinish(List<MktUserDto> dataList) {
        BeanUtil.trimProperty(dataList);
        dataList.stream().forEach(item -> { buildItemInsert(item); });
        if(insertForUserList.size() > 0) {
            userService.insertBatch(insertForUserList);
        }
        if(insertForCourseMasterList.size() > 0) {
            insertForCourseMasterList.stream().forEach(item -> {
                String loginName = item.getStudentId();
                Optional<User> user = insertForUserList.stream().filter(c -> c.getLoginName().equals(loginName) ).findFirst();
                if(user.isPresent()){
                    item.setStudentId(user.get().getId());
                }else{
                    item.setStudentId(userService.findByLoginName(loginName).getId());
                }
            });
            courseMasterService.insertBatch(insertForCourseMasterList);
        }
    }

    @Override
    protected void doNext(MktUserDto data, int index, List<String> messages) {
        String major = data.getMajor();
        //1.获取所有专业
        List<ProfilesVO> list = profilesService.getList("Major");
        List<String> majorList = list.stream().map(ProfilesVO::getValue).collect(Collectors.toList());
        if(!majorList.contains(major)){
            messages.add("未找到相关专业");
        }
        //2.验证用户
//        List<User> userList = userService.list();
        //3.专业
//        courseMasterService.list();
    }

    private void buildItemInsert(MktUserDto data){
        String major = data.getMajor();
        String grade = UserUtil.getGrade(data.getLoginName());
        User user = userService.lambdaQuery().eq(User::getLoginName, data.getLoginName()).one();
        if(user == null){
            user = new User();
            user.setLoginName(data.getLoginName());
            user.setStudentId(data.getLoginName());
            user.setNameCh(data.getName());
            user.setNameEn(data.getName());
            user.setUserType(UserEnum.USER_TYPE.STUDENT.getCode());
            user.setEmail(data.getEmail());
            user.setExpectedYear(data.getExpectedGraduationTerm());
            user.setGrade(grade);
            user.setMajor(data.getMajor());
            insertForUserList.add(user);
        }
        CourseMaster courseMaster = courseMasterService.lambdaQuery()
                .eq(CourseMaster::getStudentId, user.getId())
                .eq(CourseMaster::getMajor, major)
                .eq(CourseMaster::getGrade, grade)
                .one();
        if(courseMaster == null){
            courseMaster = new CourseMaster();
            courseMaster.setStatus(0);
            courseMaster.setStudentId(user.getLoginName());
            courseMaster.setMajor(major);
            courseMaster.setGrade(grade);
            insertForCourseMasterList.add(courseMaster);
        }

    }
}
