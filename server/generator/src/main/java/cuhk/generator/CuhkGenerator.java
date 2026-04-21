package cuhk.generator;

import cuhk.generator.custom.UneedMarketingGenerator;

/**
 * 通用代码生成启动类
 *
 * @author dongzhi
 * @date 2020/10/15 8:39 上午
 */
public class CuhkGenerator {

    public static void main(String[] args) {
        BaseGenerator generator = new UneedMarketingGenerator("/config/generator.properties");
        generator.generate();
    }

}
