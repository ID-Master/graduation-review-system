package cn.edu.cuhk.mkt;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpringVersionTest {

    @Test
    public void getSpringVersion() {
        System.out.println(11111);
        String version = SpringVersion.getVersion();
        String version1 = SpringBootVersion.getVersion();
        System.out.println(version);
        System.out.println(version1);

        String[] expectedYearArray = "2024-25 Summer".replaceFirst(" ", ":").split(":");
        String a1 = expectedYearArray[0];
        String a2 = expectedYearArray[1];

        String noteSeven = "<p>根据毕业自查表填写情况，本人是否<b>申报2022-23学年、第2学期毕业</b>？</p><p>Based on the indicated information on the self-check form, will you declare graduation on <font size=\"3\"><b style=\"\">Term 2 of&nbsp;</b><b style=\"\">AY2022-23</b></font><b style=\"font-size: 14px;\">,</b>?</p><p>Remarks:<br/></p><p>(a) The declaration on this form is limited to this graduation self-check only, for the final graduation check result, please follow the Registry Office result.</p><p>(b) If you plan to graduate earlier than your expected graduation term, you MUST apply for a \"Shortening Period of Study\" and follow the Registry Office's policies and instructions (<a href=\"https://registry.cuhk.edu.cn/en/page/196\" target=\"_blank\">https://registry.cuhk.edu.cn/en/page/196</a>).</p><p>(c) If you plan to graduate later than your expected graduation term, you should apply for a \"Change of Expected Graduation Term\", you could find the application form via the link&nbsp;<span style=\"font-size: 14px;\">&nbsp;(</span><a href=\"http://dpsite02.cuhk.edu.cn/registry-prod/sites/registry.prod.dpsite02.cuhk.edu.cn/files/2021-09/Application%20for%20Change%20of%20Expected%20Graduation%20Term.pdf\" target=\"_blank\" style=\"font-size: 14px;\">http://dpsite02.cuhk.edu.cn/registry-prod/sites/registry.prod.dpsite02.cuhk.edu.cn/files/2021-09/Application%20for%20Change%20of%20Expected%20Graduation%20Term.pdf</a><span style=\"font-size: 14px;\">)</span>and submit the application before the deadline as the Registry's email notification.&nbsp;</p>";
        String formatNoteSeven = noteSeven.replaceAll("\\d{4}-\\d{2}", a1).replaceAll("Term \\d{1}", a2);

        if (a2.equals("Term 1") || a2.equals("Term 2")) {
            formatNoteSeven = formatNoteSeven.replaceAll("第\\d{1}学", "第" + a2.split(" ")[1] + "学");
        }
        if (a2.equals("Summer")) {
            formatNoteSeven = formatNoteSeven.replaceAll("、第\\d{1}学期", "");
        }

        System.out.println(formatNoteSeven);

    }

}
