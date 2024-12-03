package com.bupt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;


@Configuration
public class RestTemplateConfig {
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        // 创建 RestTemplate 实例， 我这里使用的OkHttp
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
        // 替换默认的 Jackson 消息转换器实现
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> next = iterator.next();
            if (next instanceof MappingJackson2HttpMessageConverter) {
                iterator.remove();
                break;
            }
        }
        messageConverters.add(new CustomJacksonHttpMessageConverter());
        return restTemplate;
    }
}
