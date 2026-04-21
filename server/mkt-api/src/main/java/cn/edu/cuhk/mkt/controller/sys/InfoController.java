package cn.edu.cuhk.mkt.controller.sys;

import cn.edu.cuhk.mkt.converter.sys.InfoConverter;
import cn.edu.cuhk.mkt.entity.sys.Info;
import cn.edu.cuhk.mkt.entity.sys.InfoVO;
import cn.edu.cuhk.mkt.service.auth.AuthService;
import cn.edu.cuhk.mkt.service.sys.InfoService;
import com.uneed.common.core.date.DateUtil;
import com.uneed.common.core.date.pattern.DateFormatPattern;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * 系统模块-用户表 前端控制器
 *
 * @author taok
 * @date 2021-08-13
 */
@Slf4j
@Api(tags = {"系统模块-系统配置"})
@RestController
@RequestMapping("${adminPath}/sys/info")
public class InfoController extends AbstractRestController<InfoService, Info, InfoConverter, InfoVO> {
    @Autowired
    private AuthService authService;

    /**
     * 查询数据
     */
    @ApiOperation(value = "获取数据详情接口", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = "get")
    public Result<InfoVO> get() {
        log.info("====> /sys/info/get, vo=");
        InfoVO vo = new InfoVO();
        Optional<Info> optionalInfo = service.list().stream().findFirst();
        if(optionalInfo.isPresent()){
            vo = converter.toVO(optionalInfo.get());
            String startTimeStr = vo.getStartTime();
            String endTimeStr = vo.getEndTime();
            DateFormatPattern dateFormatPattern = DateFormatPattern.COMMON_DATE;

            if(DateUtil.getFormatPattern(startTimeStr) == dateFormatPattern){
                startTimeStr += " 00:00:00";
            }
            if(DateUtil.getFormatPattern(endTimeStr) == dateFormatPattern){
                endTimeStr += " 23:59:59";
            }
            Date startTime = DateUtil.toDate(startTimeStr);
            Date endTime = DateUtil.toDate(endTimeStr);
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime begin = startTime.toInstant().atZone(zoneId).toLocalDateTime();
            LocalDateTime end = endTime.toInstant().atZone(zoneId).toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(begin) && now.isBefore(end)){
                vo.setInvalid(Boolean.FALSE);
            }else{
                vo.setInvalid(Boolean.TRUE);
            }
        }
        return Result.ok(vo);
    }

    /**
     * 修改数据
     *
     * @param vo VO对象
     * @return Result<String> 修改结果，并填充受影响行数
     */
    @ApiOperation(value = "修改数据接口，返回受影响行数", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "update")
    public Result<Integer> update(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) InfoVO vo) {
        log.info("====> /sys/info/update, vo=" + JsonUtil.toJson(vo));
        return Result.ok(service.updateInfo(vo));
    }

}
