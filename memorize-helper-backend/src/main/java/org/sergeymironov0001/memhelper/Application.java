package org.sergeymironov0001.memhelper;

import lombok.extern.slf4j.Slf4j;
import org.sergeymironov0001.memhelper.configs.MainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;


@SpringBootApplication
@Import(MainConfig.class)
@Slf4j
public class Application {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication application = new SpringApplication(Application.class);
        ConfigurableApplicationContext run = application.run(args);
        log.info(getStartupInfo(run.getEnvironment()));
    }

    private static String getStartupInfo(Environment env) throws UnknownHostException {
        return String.format(
                "Access URLs:\n----------------------------------------------------------\n\t" +
                        "Service: \t%s\n\t" +
                        "Local: \t\thttp://127.0.0.1:%s\n\t" +
                        "External: \thttp://%s:%s\n\t" +
                        "Profiles: \t%s\n\t" +
                        "----------------------------------------------------------\n\t",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                Arrays.toString(env.getActiveProfiles())
        );
    }
}
