package org.sergeymironov0001.memhelper.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.sergeymironov0001.memhelper.controllers.FilesStorageController;
import org.sergeymironov0001.memhelper.controllers.MemorizeTasksController;
import org.sergeymironov0001.memhelper.repositories.IFilesStorage;
import org.sergeymironov0001.memhelper.services.IMemorizeTaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class ControllersConfig {

    @Bean
    public FilesStorageController filesController(IFilesStorage filesRepository) {
        return new FilesStorageController(filesRepository);
    }

    @Bean
    public MemorizeTasksController memorizeTasksController(IMemorizeTaskService memorizeTaskService) {
        return new MemorizeTasksController(memorizeTaskService);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

//    @Bean
//    public CommonsMultipartResolver commonsMultipartResolver() {
//        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//        commonsMultipartResolver.setMaxUploadSize(-1);
//        return commonsMultipartResolver;
//    }
}
