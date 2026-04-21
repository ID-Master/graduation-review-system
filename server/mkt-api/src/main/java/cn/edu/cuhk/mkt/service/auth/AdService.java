package cn.edu.cuhk.mkt.service.auth;

public interface AdService {

    /**
     * 同步学生数据
     */
    void syncAdStudent();

    /**
     * 同步老师数据
     */
    void syncAdTeacher();

    /**
     * 初始化学生数据
     */
    void initAdStudent();

    /**
     * 初始化老师数据
     */
    void initAdTeacher();

}
