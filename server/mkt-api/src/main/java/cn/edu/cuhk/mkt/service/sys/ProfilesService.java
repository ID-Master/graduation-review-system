package cn.edu.cuhk.mkt.service.sys;

import cn.edu.cuhk.mkt.entity.ad.CourseMajorDTO;
import cn.edu.cuhk.mkt.entity.sys.Profiles;
import cn.edu.cuhk.mkt.entity.sys.ProfilesVO;
import com.uneed.common.mybatis.base.SuperService;

import java.util.List;

/**
 * 系统模块-全局配置表  服务接口
 *
 * @author taok
 * @date 2021-08-17
 */
public interface ProfilesService extends SuperService<Profiles> {

    /**
     * 初始化专业数据
     * @param majors
     */
    void initMajors(List<CourseMajorDTO> majors);

    List<ProfilesVO> getList(String code);

}