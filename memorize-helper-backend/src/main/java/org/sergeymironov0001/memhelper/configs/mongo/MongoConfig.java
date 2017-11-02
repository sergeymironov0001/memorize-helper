package org.sergeymironov0001.memhelper.configs.mongo;

import org.sergeymironov0001.memhelper.repositories.IFilesStorage;
import org.sergeymironov0001.memhelper.repositories.mongo.IMongoFileInfoRepository;
import org.sergeymironov0001.memhelper.repositories.mongo.MongoFilesStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
        "org.sergeymironov0001.memhelper.repositories.mongo",
        "org.sergeymironov0001.memhelper.repositories"
})
public class MongoConfig {
    @Bean
    public IFilesStorage filesRepository(GridFsTemplate gridFsTemplate,
                                         IMongoFileInfoRepository mongoFileInfoRepository) {
        return new MongoFilesStorage(gridFsTemplate, mongoFileInfoRepository);
    }
}
