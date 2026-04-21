package cn.edu.cuhk.mkt.common.util;

import cn.edu.cuhk.mkt.common.enums.UserEnum;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户工具类
 *
 * @author taokai
 */
public class UserUtil {

    /**
     * 查询当前用户
     * @return
     */
    public static UserInfo getUserInfo(){
        return UserSession.getUser();
    }

    /**
     * 判断当前用户是否为学生
     *
     * @return 是：true, 否：false
     */
    public static boolean isStudent(){
        UserInfo userInfo = getUserInfo();
        if(userInfo.getUserType().equals(UserEnum.USER_TYPE.STUDENT.getCode())){
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户必须是学生
     */
    public static String mustStudent(){
        AssertUtil.isFalse(isStudent(), "非学生身份，不允许操作");
        return getUserInfo().getUserType();
    }

    /**
     * 判断当前用户是否为老师
     *
     * @return 是：true, 否：false
     */
    public static boolean isTeacher(){
        if(getUserInfo().getUserType().equals(UserEnum.USER_TYPE.TEACHER.getCode())){
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户必须是老师
     */
    public static String mustTeacher(){
        AssertUtil.isFalse(isTeacher(), "非老师身份，不允许操作");
        return getUserInfo().getUserType();
    }

    /**
     * 根据学号转换年级
     * @param loginName
     * @return
     */
    public static String getGrade(String loginName){
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence)loginName);
        boolean result = matcher.matches();
        if (result) {
            return "20"+loginName.substring(1,3);
        }
        else{
            return "2018";
        }
    }

}
