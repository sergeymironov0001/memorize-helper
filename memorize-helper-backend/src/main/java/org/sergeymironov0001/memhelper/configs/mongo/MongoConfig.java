package org.sergeymironov0001.memhelper.configs.mongo;

import org.sergeymironov0001.memhelper.repositories.IFilesRepository;
import org.sergeymironov0001.memhelper.repositories.mongo.IMongoFileInfoRepository;
import org.sergeymironov0001.memhelper.repositories.mongo.MongoFilesRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
        "org.sergeymironov0001.memhelper.repositories.mongo"
})
public class MongoConfig {
    @Bean
    public IFilesRepository filesRepository(GridFsTemplate gridFsTemplate,
                                            IMongoFileInfoRepository mongoFileInfoRepository) {
        return new MongoFilesRepository(gridFsTemplate, mongoFileInfoRepository);
    }
}
