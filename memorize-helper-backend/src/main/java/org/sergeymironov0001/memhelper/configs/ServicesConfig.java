package org.sergeymironov0001.memhelper.configs;

import org.sergeymironov0001.memhelper.repositories.IFilesStorage;
import org.sergeymironov0001.memhelper.repositories.IMemorizeTaskRepository;
import org.sergeymironov0001.memhelper.services.IMemorizeTaskService;
import org.sergeymironov0001.memhelper.services.MemorizeTaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    public IMemorizeTaskService memorizeTaskService(IMemorizeTaskRepository memorizeTaskRepository,
                                                    IFilesStorage filesStorage) {
        return new MemorizeTaskService(memorizeTaskRepository, filesStorage);
    }
}
