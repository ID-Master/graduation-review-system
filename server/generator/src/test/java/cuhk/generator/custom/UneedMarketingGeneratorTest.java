package cuhk.generator.custom;

import cuhk.generator.BaseGenerator;

/**
 * CoracleMarketingGenerator Tester.
 *
 * @author huangad@coracle.com
 * @version 1.0
 * @since 04/19/2018
 */
public class UneedMarketingGeneratorTest {


    /**
     * 测试自动生成代码
     *
     */
    public static void main(String[] args) {
//        BaseGenerator generator=new UneedMarketingGenerator("D:\\Generator\\conf\\generator.properties");
        BaseGenerator generator=new UneedMarketingGenerator("/Users/huangad/generator/conf/generator.properties");
        generator.generate();
    }

}