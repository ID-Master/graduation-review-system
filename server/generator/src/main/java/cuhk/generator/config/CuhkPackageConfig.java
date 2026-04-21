package cuhk.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.PackageConfig;

/**
 * 三棵树自定义包配置
 *
 * @author dongzhi
 * @date 2020/10/15 9:40 上午
 */
public class CuhkPackageConfig extends PackageConfig {

    @Override
    public String getParent() {
        return super.getParent().replace("." + getModuleName(), "");
    }

    @Override
    public String getEntity() {
        if (StringUtils.isNotBlank(getModuleName())) {
            return super.getEntity() + StringPool.DOT + getModuleName();
        }
        return super.getEntity();
    }

    @Override
    public String getService() {
        if (StringUtils.isNotBlank(getModuleName())) {
            return super.getService() + StringPool.DOT + getModuleName();
        }
        return super.getService();
    }

    @Override
    public String getServiceImpl() {
        if (StringUtils.isNotBlank(getModuleName())) {
            return super.getServiceImpl() + StringPool.DOT + getModuleName();
        }
        return super.getServiceImpl();
    }

    @Override
    public String getMapper() {
        if (StringUtils.isNotBlank(getModuleName())) {
            return super.getMapper() + StringPool.DOT + getModuleName();
        }
        return super.getMapper();
    }

    @Override
    public String getController() {
        if (StringUtils.isNotBlank(getModuleName())) {
            return super.getController() + StringPool.DOT + getModuleName();
        }
        return super.getController();
    }
}
