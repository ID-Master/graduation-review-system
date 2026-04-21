package cn.edu.cuhk.mkt.controller.sys;

import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.converter.sys.UserConverter;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.entity.sys.UserVO;
import cn.edu.cuhk.mkt.param.auth.LoginParam;
import cn.edu.cuhk.mkt.service.auth.AuthService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.alibaba.fastjson.JSON;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

/**
 * 系统模块-用户表 前端控制器
 *
 * @author taok
 * @date 2021-08-13
 */
@Slf4j
@Api(tags = {"系统模块-用户表"})
@RestController
@RequestMapping("${adminPath}/sys/user")
public class UserController extends AbstractRestController<UserService, User, UserConverter, UserVO> {
    @Autowired
    private AuthService authService;

    /**
     * 新增数据
     *
     * @param vo VO对象
     * @return Result<String> 新增结果，并填充数据id
     */
    @ApiOperation(value = "新增数据接口", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "insert")
    public Result<String> insert(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) UserVO vo) {
        log.info("====> /sys/user/insert, vo=" + JsonUtil.toJson(vo));
        if(superAdmin().equals(Boolean.FALSE)){
            AssertUtil.isNull(null, "非管理员，禁止新增用户");
        }
        return super.insert(vo, null);
    }

    /**
     * 修改数据
     *
     * @param vo VO对象
     * @return Result<String> 修改结果，并填充受影响行数
     */
    @ApiOperation(value = "修改数据接口，返回受影响行数", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "update")
    public Result<String> update(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) UserVO vo) {
        log.info("====> /sys/user/update, vo=" + JsonUtil.toJson(vo));
        if(superAdmin().equals(Boolean.FALSE)){
            AssertUtil.isNull(null, "非管理员，禁止修改用户");
        }
        return super.update(vo, null);
    }

    /**
     * 根据id删除数据
     *
     * @param id 数据主键
     * @return Result<String> 删除结果，并填充受影响行数
     */
    @ApiOperation(value = "删除数据接口，返回受影响行数", notes = "根据数据id，从数据库中删除其对应的数据")
    @GetMapping(value = "remove/{id}")
    public Result<String> remove(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /sys/user/remove/{id}, id=" + id);
        if(superAdmin().equals(Boolean.FALSE)){
            AssertUtil.isNull(null, "非管理员，禁止删除用户");
        }
        return super.remove(id, null);
    }

    /**
     * 根据id设置数据有效性
     *
     * @param id 数据主键
     * @return Result<String> 设置有效性结果结果，并填充受影响行数
     */
    @ApiOperation(value = "设置数据有效性接口，返回受影响行数", notes = "根据数据id，设置对应数据的有效性")
    @GetMapping(value = "active/{id}")
    public Result<String> active(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /sys/user/active/{id}, id=" + id);
        if(superAdmin().equals(Boolean.FALSE)){
            AssertUtil.isNull(null, "非管理员，禁止修改用户状态");
        }
        return super.active(id, null);
    }

    /**
     * 根据id获取数据详情
     *
     * @param id 数据主键
     * @return Result<UserVO> 详情结果
     */
    @ApiOperation(value = "获取数据详情接口", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = "detail/{id}")
    public Result<UserVO> detail(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /sys/user/detail/{id}, id=" + id);
        return super.detail(id, null);
    }

    /**
     * 根据条件参数获取分页后的列表数据
     *
     * @param search 条件参数
     * @return Result<PageData<UserVO>> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "获取数据列表接口", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @PostMapping(value = "list")
    public Result<PageData<UserVO>> list(@RequestBody @ApiParam(value = "条件参数", required = true) PageSearch<UserVO> search) {
        log.info("====> /sys/user/list, search=" + JsonUtil.toJson(search));
        return super.list(search, null);
    }

    @RequestMapping("info")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("UserController-获取user信息:{}", JSON.toJSON(authentication));
        log.info("UserController-获取user信息:{}", JSON.toJSON(principal));
        LoginParam loginParam = new LoginParam();
        Map<String, Object> attributes = principal.getAttributes();
        String keyTitle = "title";
        String keyUniqueName = "unique_name";
        String keyUpn = "upn";
        if(attributes.get(keyTitle) != null){
            loginParam.setTitle(attributes.get(keyTitle).toString());
        }
        if(attributes.get(keyUniqueName) != null){
            loginParam.setUniqueName(attributes.get(keyUniqueName).toString());
        }
        if(attributes.get(keyUpn) != null){
            loginParam.setUpn(attributes.get(keyUpn).toString());
        }
        log.info("=========>loginParam: {}", JSON.toJSONString(loginParam));
        authService.loginAdfs(loginParam);
        return Collections.singletonMap("name", principal.getAttributes().get("upn"));
    }

    /**
     * 判断账号是否为管理员
     * @return
     */
    private Boolean superAdmin() {
        UserInfo userInfo = UserSession.getUser();
        return userInfo.getSuperAdmin();
    }

}
