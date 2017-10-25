package org.sergeymironov0001.memhelper.config;

import org.sergeymironov0001.memhelper.config.mongo.MongoConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MongoConfig.class
})
public class MainConfig {
}
