package ododock.webserver.security.config;

import ododock.webserver.config.domain.StorageProperties;
import ododock.webserver.web.ResourcePath;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private final String storageRootDir;

    public StaticResourceConfig(StorageProperties storageProperties) {
        this.storageRootDir = storageProperties.fileService().rootDir();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(ResourcePath.DOCS + "/**")
                .addResourceLocations("classpath:" + ResourcePath.DOCS);

        String location;
        if (storageRootDir.startsWith("/")) {
            location = "file:" + storageRootDir + File.separator;
        } else {
            location = "file:" + Path.of(storageRootDir).toAbsolutePath() + File.separator;
        }

        registry
                .addResourceHandler("/images/**")
                .addResourceLocations(location);
    }

}
