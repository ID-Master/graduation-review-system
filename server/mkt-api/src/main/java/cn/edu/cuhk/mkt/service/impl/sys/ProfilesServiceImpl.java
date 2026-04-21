package cn.edu.cuhk.mkt.service.impl.sys;

import cn.edu.cuhk.mkt.common.enums.StatusEnum;
import cn.edu.cuhk.mkt.converter.sys.ProfilesConverter;
import cn.edu.cuhk.mkt.entity.ad.CourseMajorDTO;
import cn.edu.cuhk.mkt.entity.sys.Profiles;
import cn.edu.cuhk.mkt.entity.sys.ProfilesVO;
import cn.edu.cuhk.mkt.mapper.sys.ProfilesMapper;
import cn.edu.cuhk.mkt.service.sys.ProfilesService;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 系统模块-全局配置表 服务实现
 *
 * @author taok
 * @date 2021-08-17
 */
@Service
public class ProfilesServiceImpl extends SuperServiceImpl<ProfilesMapper, Profiles> implements ProfilesService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initMajors(List<CourseMajorDTO> majors) {
        if(CollectionUtils.isEmpty(majors)){
            return;
        }

        // 清空历史专业（逻辑删除）
        //Map<String, Object> delMap = new HashMap(2);
        //delMap.put(AttributeUtil.getTableFieldValue(Profiles::getCode), "Major");
        //super.mapper.deleteByMap(delMap);

        // 清空历史专业（物理删除）
        Profiles delCondition = new Profiles();
        delCondition.setCode("Major");
        super.mapper.deleteByCondition(delCondition);

        List<Profiles> majorList = new ArrayList<>();
        Optional.ofNullable(majors).orElse(new ArrayList<>()).forEach(majorDTO -> {
            String code = StringUtils.defaultString(majorDTO.getCode());
            String name = StringUtils.defaultString(majorDTO.getName());
            String value = StringUtils.defaultString(majorDTO.getValue());
            String sortIndexStr = StringUtils.defaultString(majorDTO.getSortIndex());
            Integer sortIndex = 0;
            try{
                sortIndex = Integer.parseInt(sortIndexStr);
            }catch (Exception e){
            }
            Profiles majorProfile = new Profiles();
            majorProfile.setName(name);
            majorProfile.setCode(code);
            majorProfile.setValue(value);
            majorProfile.setStatus(StatusEnum.EFFECTIVE.getCode());
            majorProfile.setSortIndex(sortIndex);
            majorList.add(majorProfile);
        });
        // 批量写入
        super.insertBatch(majorList);
    }

    @Override
    public List<ProfilesVO> getList(String code) {
        List<Profiles> list = super.lambdaQuery()
                .eq(Profiles::getCode, code)
                .eq(Profiles::getStatus, StatusEnum.EFFECTIVE.getCode())
                .orderByAsc(Profiles::getSortIndex)
                .list();
        return new ProfilesConverter().toVO(list);
    }

}
