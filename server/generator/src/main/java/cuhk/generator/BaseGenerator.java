package cuhk.generator;

import com.uneed.common.core.lang.ObjectUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author huangad@coracle.com
 * @since 2017/4/15
 */
public abstract class BaseGenerator {

    protected String configPath;

    protected Properties props;

    protected String output = "D:\\code";

    protected static final String CFG_PREFIX = "cfg.";

    protected void initProperties() throws IOException {
        Resource resource = getResource(configPath);
        props = PropertiesLoaderUtils.loadProperties(resource);
        //需要对windows路径进行转换
        output = props.getProperty("gc.output", output).replaceAll("\\\\", "/");
    }

    private Resource getResource(String configPath) {
        if (ObjectUtil.isEmpty(configPath)) {
            configPath = "config/generator.properties";
        }
        File file = new File(configPath);
        if (file.exists()) {
            return new FileSystemResource(file);
        }
        return new ClassPathResource(configPath);
    }

    protected abstract void execute();

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public void generate() {
        try {
            //初始化配置文件的配置
            initProperties();
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
