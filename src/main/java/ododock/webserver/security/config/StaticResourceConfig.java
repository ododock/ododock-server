package ododock.webserver.security.config;

import ododock.webserver.web.ResourcePath;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(ResourcePath.DOCS + "/**")
                .addResourceLocations("classpath:" + ResourcePath.DOCS);

        registry
                .addResourceHandler("/images/**") // URL 패턴
                .addResourceLocations("file:images/"); // 로컬 디렉토리 (루트 기준)
    }

}
