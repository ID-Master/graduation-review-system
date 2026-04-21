package cn.edu.cuhk.mkt.service.impl.sys;

import cn.edu.cuhk.mkt.converter.sys.InfoConverter;
import cn.edu.cuhk.mkt.entity.sys.Info;
import cn.edu.cuhk.mkt.entity.sys.InfoVO;
import cn.edu.cuhk.mkt.mapper.sys.InfoMapper;
import cn.edu.cuhk.mkt.service.sys.InfoService;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 系统模块-用户表 服务实现
 *
 * @author taok
 * @date 2021-08-13
 */
@Service
public class InfoServiceImpl extends SuperServiceImpl<InfoMapper, Info> implements InfoService {

    @Override
    public Integer updateInfo(InfoVO vo) {
        List<Info> list = list();
        if(list.size() == 0){
            Info info = new InfoConverter().toEntity(vo);
            return insert(info);
        }else{
            Info info = list.get(0);
            info.setMessage(vo.getMessage());
            info.setStartTime(vo.getStartTime());
            info.setEndTime(vo.getEndTime());
            info.setNoteOne(vo.getNoteOne());
            info.setNoteSeven(vo.getNoteSeven());
            info.setNoteEight(vo.getNoteEight());
            info.setTeacherList(vo.getTeacherList());
            return update(info);
        }
    }

    @Override
    public Info getOne() {
        Optional<Info> info = list().stream().findFirst();
        if(info.isPresent()){
            return info.get();
        }
        return null;
    }
}
