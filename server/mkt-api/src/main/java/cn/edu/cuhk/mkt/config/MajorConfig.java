package cn.edu.cuhk.mkt.config;

import cn.edu.cuhk.mkt.entity.ad.CourseMajorDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author taokai
 */
@Data
@Component
@ConfigurationProperties(prefix = "course")
public class MajorConfig {

    private List<CourseMajorDTO> majors;

}
