package cn.edu.cuhk.mkt.common.util;

import cn.edu.cuhk.mkt.entity.report.MktCourseReport;
import cn.edu.cuhk.mkt.entity.report.MktCourseReportHead;
import cn.edu.cuhk.mkt.entity.report.MktUserDto;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.support.api.Result;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.joda.time.DateTime;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtil {

    /**
     * 导出课程数据（填充MKT模板）
     *
     */
    public static void exportCourseWithTemplate(MktCourseReportHead mktCourseReportHead, List<MktCourseReport> courseReportList, HttpServletResponse response) throws IOException {
        // 模板 {} 代表普通变量 {.} 代表是list的变量，特殊字符 用"\{","\}"代替
        // 此处getResourceAsStream 用于获取服务器打包后的Excel模板文件流;
        InputStream resourceAsStream = new ClassPathResource("files"+ File.separator+"MktReportFormTemplcate"+ ExcelTypeEnum.XLSX.getValue())
                .getInputStream();
        ZipSecureFile.setMinInflateRatio(-1.0d);
        String fileName = URLEncoder.encode("MKT报表" + DateTime.now().toString("yyyyMMddHHmmss"), "UTF-8");
        OutputStream outputStream = ExcelUtil.getOutputStream(fileName, response, ExcelTypeEnum.XLSX);
        // 读取Excel 根据指定模板导出
        ExcelWriter excelWriter = EasyExcel.write(outputStream)
                .withTemplate(resourceAsStream)
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        System.out.println("---mktCourseReportHead--->" + JsonUtil.toJson(mktCourseReportHead));
        FillConfig headConfig = FillConfig.builder().forceNewRow(Boolean.FALSE).build();
        excelWriter.fill(mktCourseReportHead, headConfig, writeSheet);
        // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(courseReportList, fillConfig, writeSheet);
        excelWriter.finish();
    }

    /**
     * 下载上传学生模板
     *
     */
    public static void writeExcel(HttpServletResponse response,String fileName,String sheetName) {
        MktUserDto mktUserDto = new MktUserDto();
        List<MktUserDto> data = Lists.newArrayList();
        EasyExcel.write(getOutputStream(fileName, response, ExcelTypeEnum.XLSX) , MktUserDto.class).sheet(sheetName).doWrite(data);
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    public static OutputStream getOutputStream(String fileName, HttpServletResponse response, ExcelTypeEnum excelTypeEnum) {
        try {
            //设置响应导出
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + excelTypeEnum.getValue());
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            try {
                response.getWriter().println(JSON.toJSONString(Result.fail("下载文件失败")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return null;
    }

}
