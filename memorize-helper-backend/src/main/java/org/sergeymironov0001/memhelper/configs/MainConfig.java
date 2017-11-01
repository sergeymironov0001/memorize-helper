package org.sergeymironov0001.memhelper.configs;

import org.sergeymironov0001.memhelper.configs.mongo.MongoConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MongoConfig.class,
        RepositoriesConfig.class,
        ControllersConfig.class
})
public class MainConfig {
}
