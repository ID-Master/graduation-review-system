package cuhk.generator.custom;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.lang.ObjectUtil;
import cuhk.generator.BaseGenerator;
import cuhk.generator.config.CuhkPackageConfig;

import java.util.Enumeration;
import java.util.Map;

import static com.uneed.common.core.convert.Convert.toBool;

/**
 * 自定义代码生成
 *
 * @author huangad@coracle.com
 * @since 2018-04-17
 */
public class UneedMarketingGenerator extends BaseGenerator {

    /**
     * 公共字段常量
     */
    public static final String PUBLIC_FIELDS = "id,create_by,create_time,update_by,update_time,remove_flag";

    /**
     * 默认实体类父类
     */
    public static final String SUPER_ENTITY_CLASS = "com.uneed.common.mybatis.model.SuperModel";

    /**
     * 默认mapper类父类
     */
    public static final String SUPER_MAPPER_CLASS = "com.uneed.common.mybatis.base.SuperMapper";

    /**
     * 默认service类父类
     */
    public static final String SUPER_SERVICE_CLASS = "com.uneed.common.mybatis.base.SuperService";

    /**
     * 默认serviceImpl类父类
     */
    public static final String SUPER_SERVICE_IMPL_CLASS = "com.uneed.common.mybatis.base.SuperServiceImpl";

    /**
     * 默认controller类父类
     */
    public static final String SUPER_CONTROLLER_CLASS = "com.uneed.common.support.base.AbstractRestController";

    /**
     * 默认父级路径
     */
    public static final String PARENT_PATH = "com.uneed.cloud.platform";

    /**
     * 执行该方法生成代码
     */
    public static void main(String[] args) {
        BaseGenerator generator = new UneedMarketingGenerator(null);
        generator.generate();
    }

    public UneedMarketingGenerator(String configPath) {
        super.configPath = configPath;
    }

    /**
     * 配置代码生成规则，执行代码生成
     */
    @Override
    protected void execute() {
        AutoGenerator autoGenerator = new AutoGenerator();
        // 全局配置
        GlobalConfig globalConfig = getGlobalConfig();
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = getDataSourceConfig();
        autoGenerator.setDataSource(dataSourceConfig);

        // 策略配置
        StrategyConfig strategyConfig = getStrategyConfig();
        autoGenerator.setStrategy(strategyConfig);

        // 包配置
        PackageConfig packageConfig = getPackageConfig();
        autoGenerator.setPackageInfo(packageConfig);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig injectionConfig = getInjectionConfig(globalConfig, packageConfig);
        autoGenerator.setCfg(injectionConfig);

        //模板配置
        TemplateConfig templateConfig = getTemplateConfig(globalConfig);

        autoGenerator.setTemplate(templateConfig);

        // 执行生成
        autoGenerator.execute();
    }

    /**
     * 生成Provider.java
     */
    private FileOutConfig getProviderConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        return new FileOutConfig("/template/provider.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return globalConfig.getOutputDir() + "/" + packageConfig.getParent().replaceAll("\\.", "/") + "/" + "provider"
                        + "/" + tableInfo.getEntityName() + "Provider.java";
            }
        };
    }

    /**
     * 生成Fallback.java
     */
    private FileOutConfig getFallbackConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        return new FileOutConfig("/template/fallback.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return globalConfig.getOutputDir() + "/" + packageConfig.getParent().replaceAll("\\.", "/") + "/"
                        + "client/fallback" + "/" + tableInfo.getEntityName() + "Fallback.java";
            }
        };
    }

    /**
     * 生成Client.java
     */
    private FileOutConfig getClientConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        return new FileOutConfig("/template/client.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return globalConfig.getOutputDir() + "/" + packageConfig.getParent().replaceAll("\\.", "/") + "/" + "client"
                        + "/" + tableInfo.getEntityName() + "Client.java";
            }
        };
    }

    /**
     * 生成Converter.java
     */
    private FileOutConfig getConverterConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        final String template = globalConfig.isKotlin() ? "/template/converter.kt.vm" : "/template/converter.java.vm";
        return new FileOutConfig(template) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return globalConfig.getOutputDir() + "/" + packageConfig.getParent().replaceAll("\\.", "/") + "/" + "converter" + "/"
                        + packageConfig.getModuleName() + "/" + tableInfo.getEntityName() + (globalConfig.isKotlin() ? "Converter.kt" : "Converter.java");
            }
        };
    }

    /**
     * 生成DTO.java
     */
    private FileOutConfig getDTOConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        final String template = globalConfig.isKotlin() ? "/template/dto.kt.vm" : "/template/dto.java.vm";
        return new FileOutConfig(template) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return globalConfig.getOutputDir() + "/" + packageConfig.getParent().replaceAll("\\.", "/") + "/" + "entity" + "/"
                        + packageConfig.getModuleName() + "/" + tableInfo.getEntityName() + (globalConfig.isKotlin() ? "DTO.kt" : "DTO.java");
            }
        };
    }

    /**
     * 生成VO.java
     */
    private FileOutConfig getVOConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        final String template = globalConfig.isKotlin() ? "/template/vo.kt.vm" : "/template/vo.java.vm";
        return new FileOutConfig(template) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return globalConfig.getOutputDir() + "/" + packageConfig.getParent().replaceAll("\\.", "/") + "/" + "entity" + "/"
                        + packageConfig.getModuleName() + "/" + tableInfo.getEntityName() + (globalConfig.isKotlin() ? "VO.kt" : "VO.java");
            }
        };
    }

    /**
     * 生成mapper.xml
     */
    private FileOutConfig getMapperConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        return new FileOutConfig("/template/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return globalConfig.getOutputDir() + "/" + "mapper" + "/" + packageConfig.getModuleName() + "/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        };
    }

    /**
     * 全局策略globalConfig配置
     *
     * @return GlobalConfig
     */
    private GlobalConfig getGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();

        // 生成文件的输出目录
        globalConfig.setOutputDir(props.getProperty("gc.output"));
        // 是否覆盖已有文件
        globalConfig.setFileOverride(toBool(props.getProperty("gc.fileOverride"), true));
        // 代码生成完毕后是否打开输出目录
        globalConfig.setOpen(toBool(props.getProperty("gc.open"), true));
        // 是否在xml中添加二级缓存配置
        globalConfig.setEnableCache(toBool(props.getProperty("gc.enableCache"), false));
        // 开发人员
        globalConfig.setAuthor(props.getProperty("gc.author"));
        // 是否开启swagger2模式
        globalConfig.setSwagger2(toBool(props.getProperty("gc.swagger2"), true));
        // 是否开启ActiveRecord模式
        globalConfig.setActiveRecord(toBool(props.getProperty("gc.activeRecord"), false));
        // 是否开启BaseResultMap
        globalConfig.setBaseResultMap(toBool(props.getProperty("gc.baseResultMap"), false));
        // 是否开启BaseColumnList
        globalConfig.setBaseColumnList(toBool(props.getProperty("gc.baseColumnList"), false));
        // 开启 Kotlin 模式
        globalConfig.setKotlin(toBool(props.getProperty("gc.kotlin"), false));
        // 时间类型对应策略：ONLY_DATE，使用java.util.date包下的类型；SQL_PACK，使用java.sql包下的类型；TIME_PACK，使用java.time包下的类型，表示java8新时间类型
        DateType type = DateType.ONLY_DATE;
        String temp = props.getProperty("gc.dateType");
        if (DateType.SQL_PACK.name().equals(temp)) {
            type = DateType.SQL_PACK;
        }
        if (DateType.TIME_PACK.name().equals(temp)) {
            type = DateType.TIME_PACK;
        }
        globalConfig.setDateType(type);
        // 实体命名方式
        globalConfig.setEntityName(props.getProperty("gc.entityName", ""));
        // mapper命名方式
        globalConfig.setMapperName(props.getProperty("gc.mapperName", "%sMapper"));
        // mapper xml命名方式
        globalConfig.setXmlName(props.getProperty("gc.xmlName", "%sMapper"));
        // service命名方式
        globalConfig.setServiceName(props.getProperty("gc.serviceName", "%sService"));
        // service impl命名方式
        globalConfig.setServiceImplName(props.getProperty("gc.serviceImplName", "%sServiceImpl"));
        // controller命名方式
        globalConfig.setControllerName(props.getProperty("gc.controllerName", "%sController"));
        return globalConfig;
    }

    /**
     * 数据源dataSourceConfig配置
     *
     * @return DataSourceConfig
     */
    private DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName(props.getProperty("db.driverName"));
        dataSourceConfig.setUrl(props.getProperty("db.url"));
        dataSourceConfig.setUsername(props.getProperty("db.username"));
        dataSourceConfig.setPassword(props.getProperty("db.password"));
        return dataSourceConfig;
    }

    /**
     * 数据库表策略配置
     *
     * @return StrategyConfig
     */
    private StrategyConfig getStrategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        // 表示生成实体时忽略的表前缀名，可配置多个
        strategyConfig.setTablePrefix(ObjectUtil.nullToDefault(props.getProperty("sc.tablePrefix"), "").split(","));
        // 需要生成的表
        String[] includes = ObjectUtil.nullToDefault(props.getProperty("sc.include"), "").split(",");
        if (ObjectUtil.isNotEmpty(includes)) {
            strategyConfig.setInclude(includes);
        } else {
            // 需要过滤的表
            strategyConfig.setExclude(ObjectUtil.nullToDefault(props.getProperty("sc.exclude"), "").split(","));
        }
        // 自定义继承的Entity类全称，带包名
        strategyConfig.setSuperEntityClass(props.getProperty("sc.superEntityClass", SUPER_ENTITY_CLASS));
        // 自定义基础的Entity类，公共字段
        strategyConfig.setSuperEntityColumns(props.getProperty("sc.superEntityColumns", PUBLIC_FIELDS).split(","));
        // 自定义继承的Mapper类全称，带包名
        strategyConfig.setSuperMapperClass(props.getProperty("sc.superMapperClass", SUPER_MAPPER_CLASS));
        // 自定义继承的Service类全称，带包名
        strategyConfig.setSuperServiceClass(props.getProperty("sc.superServiceClass", SUPER_SERVICE_CLASS));
        // 自定义继承的ServiceImpl类全称，带包名
        strategyConfig.setSuperServiceImplClass(props.getProperty("sc.superServiceImplClass", SUPER_SERVICE_IMPL_CLASS));
        // 自定义继承的Controller类全称，带包名
        strategyConfig.setSuperControllerClass(props.getProperty("sc.superControllerClass", SUPER_CONTROLLER_CLASS));

        // 表名称映射实体类名称规则
        NamingStrategy namingStrategy = toBool(props.getProperty("sc.namingCamel"), true) ? NamingStrategy.underline_to_camel
                : NamingStrategy.no_change;
        strategyConfig.setNaming(namingStrategy);
        // 是否生成实体时，生成字段注解
        strategyConfig
                .setEntityTableFieldAnnotationEnable(toBool(props.getProperty("sc.entityTableFieldAnnotationEnable"), true));
        // 【实体】是否为lombok模型（默认 false）
        strategyConfig.setEntityLombokModel(toBool(props.getProperty("sc.entityLombokModel"), true));
        //【实体】是否为构建者模型（默认 false）
        strategyConfig.setEntityBuilderModel(toBool(props.getProperty("sc.entityBuilderModel"), false));
        // 生成 <code>@RestController</code> 控制器
        strategyConfig.setRestControllerStyle(toBool(props.getProperty("sc.restControllerStyle"), true));
        // 驼峰转连字符 <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
        strategyConfig.setControllerMappingHyphenStyle(toBool(props.getProperty("sc.controllerMappingHyphenStyle"), true));
        // 实体是否生成 serialVersionUID
        strategyConfig.setEntitySerialVersionUID(toBool(props.getProperty("sc.entitySerialVersionUID"), true));
        return strategyConfig;
    }


    /**
     * 包名配置
     *
     * @return PackageConfig
     */
    private PackageConfig getPackageConfig() {
        PackageConfig packageConfig = new CuhkPackageConfig();
        // 父包名
        packageConfig.setParent(props.getProperty("pc.parent", PARENT_PATH));
        // 父包模块名
        packageConfig.setModuleName(props.getProperty("pc.moduleName"));
        // Entity包名
        packageConfig.setEntity(props.getProperty("pc.entity", "entity"));
        // Mapper包名
        packageConfig.setMapper(props.getProperty("pc.mapper", "mapper"));
        // Service包名
        packageConfig.setService(props.getProperty("pc.service", "service"));
        // Service Impl包名
        packageConfig.setServiceImpl(props.getProperty("pc.serviceImpl", "service.impl"));
        // Controller包名
        packageConfig.setController(props.getProperty("pc.controller", "controller"));
        return packageConfig;
    }

    /**
     * 注入 injectionConfig 配置，可以在.vm文件中使用 cfg.abc 设置的值
     *
     * @param globalConfig  globalConfig
     * @param packageConfig packageConfig
     * @return InjectionConfig
     */
    private InjectionConfig getInjectionConfig(GlobalConfig globalConfig, PackageConfig packageConfig) {
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {

                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                Map<String, Object> map = Maps.newHashMap();
                String moduleName = StringUtils.isBlank(packageConfig.getModuleName()) ? "" : "." + packageConfig.getModuleName();
                //追加vo、dto、converter、client、fallback、provider路径
                map.put("voPackage", packageConfig.getParent() + "." + "entity" + moduleName);
                map.put("dtoPackage", packageConfig.getParent() + "." + "entity" + moduleName);
                map.put("converterPackage", packageConfig.getParent() + "." + "converter" + moduleName);
                map.put("clientPackage", packageConfig.getParent() + "." + "client" + moduleName);
                map.put("fallbackPackage", packageConfig.getParent() + "." + "client" + "." + "fallback" + moduleName);
                map.put("providerPackage", packageConfig.getParent() + "." + "provider" + moduleName);
                //追加converter、client、fallback、provider父类
                map.put("superConverterClass", props.getProperty("cfg.superConverterClass", "AbstractModelConverter"));
                map.put("superConverterClassPackage", props.getProperty("cfg.superConverterClassPackage",
                        "com.uneed.common.support.convert.AbstractModelConverter"));
                map.put("superClientClass", props.getProperty("cfg.superClientClass", "SuperClient"));
                map.put("superClientClassPackage", props.getProperty("cfg.superClientClassPackage",
                        "com.uneed.common.support.base.SuperClient"));
                map.put("superFallbackClass", props.getProperty("cfg.superFallbackClass", "AbstractClientFallback"));
                map.put("superFallbackClassPackage", props.getProperty("cfg.superFallbackClassPackage",
                        "com.uneed.common.support.base.AbstractClientFallback"));
                map.put("superProviderClass", props.getProperty("cfg.superProviderClass", "AbstractClientProvider"));
                map.put("superProviderClassPackage", props.getProperty("cfg.superProviderClassPackage",
                        "com.uneed.common.support.base.AbstractClientProvider"));

                //request 前缀
                map.put("requestPrefix", props.getProperty("cfg.requestPrefix", "api"));
                //controller 通用方法
                map.put("controllerActive", toBool(props.getProperty("cfg.controllerActive"), false));
                map.put("controllerImport", toBool(props.getProperty("cfg.controllerImport"), false));
                map.put("controllerExport", toBool(props.getProperty("cfg.controllerExport"), false));
                //client 前缀
                map.put("clientPrefix", props.getProperty("cfg.clientPrefix", "client"));

                //追加配置文件中的自定义配置
                Enumeration enumeration = props.propertyNames();
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement().toString();
                    if (key.startsWith(CFG_PREFIX)) {
                        map.put(key.substring(CFG_PREFIX.length()), props.getProperty(key));
                    }
                }
                this.setMap(map);
            }
        }.setFileOutConfigList(Lists.newArrayList());
        injectionConfig.getFileOutConfigList().add(getMapperConfig(globalConfig, packageConfig));
        injectionConfig.getFileOutConfigList().add(getVOConfig(globalConfig, packageConfig));
        injectionConfig.getFileOutConfigList().add(getDTOConfig(globalConfig, packageConfig));
        injectionConfig.getFileOutConfigList().add(getConverterConfig(globalConfig, packageConfig));
        boolean generateClient = toBool(props.getProperty("cfg.generateClient"), false);
        if (generateClient) {
            injectionConfig.getFileOutConfigList().add(getClientConfig(globalConfig, packageConfig));
            injectionConfig.getFileOutConfigList().add(getFallbackConfig(globalConfig, packageConfig));
            injectionConfig.getFileOutConfigList().add(getProviderConfig(globalConfig, packageConfig));
        }
        return injectionConfig;
    }

    /**
     * 模版配置
     */
    private TemplateConfig getTemplateConfig(GlobalConfig globalConfig) {
        TemplateConfig templateConfig = new TemplateConfig();
        // Mapper Xml模版
        templateConfig.setXml(null);
        if (globalConfig.isKotlin()) {
            //entity模板
            templateConfig.setEntityKt(props.getProperty("tp.entityKt", "template/entity.kt.vm"));
            // Mapper模版
            templateConfig.setMapper(props.getProperty("tp.mapper", "template/mapper.kt.vm"));
            // Service模版
            templateConfig.setService(props.getProperty("tp.service", "template/service.kt.vm"));
            // Service Impl模版
            templateConfig.setServiceImpl(props.getProperty("tp.serviceImpl", "template/serviceImpl.kt.vm"));
            // Controller模板
            templateConfig.setController(props.getProperty("tp.controller", "template/controller.kt.vm"));
        } else {
            //entity模板
            templateConfig.setEntity(props.getProperty("tp.entity", "template/entity.java.vm"));
            // Mapper模版
            templateConfig.setMapper(props.getProperty("tp.mapper", "template/mapper.java.vm"));
            // Service模版
            templateConfig.setService(props.getProperty("tp.service", "template/service.java.vm"));
            // Service Impl模版
            templateConfig.setServiceImpl(props.getProperty("tp.serviceImpl", "template/serviceImpl.java.vm"));
            // Controller模板
            templateConfig.setController(props.getProperty("tp.controller", "template/controller.java.vm"));
        }
        return templateConfig;
    }

}
