package org.sergeymironov0001.memhelper.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        RepositoriesConfig.class,
        ServicesConfig.class,
        ControllersConfig.class
})
public class MainConfig {
}
