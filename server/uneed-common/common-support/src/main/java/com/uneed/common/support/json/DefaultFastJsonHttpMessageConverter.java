package com.uneed.common.support.json;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.uneed.common.core.collection.CollectionUtil;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * 重写FastJsonHttpMessageConverter，为converter设置MediaType
 *
 * @author diablo
 * @date 2018/5/15
 */
public class DefaultFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    public DefaultFastJsonHttpMessageConverter() {
        super();
        List<MediaType> supportedMediaTypes = CollectionUtil.newArrayList();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        super.setSupportedMediaTypes(supportedMediaTypes);
        super.setFastJsonConfig(new DefaultFastJsonConfig());
    }
}
