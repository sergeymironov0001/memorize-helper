package org.sergeymironov0001.memhelper.repositories;

import com.mongodb.Mongo;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sergeymironov0001.memhelper.configs.RepositoriesConfig;
import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoFilesRepositoryIntegrationTest.IFilesRepositoryTestConfig.class})
public class MongoFilesRepositoryIntegrationTest {

    @Autowired
    private IFilesRepository filesRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    public void save() throws Exception {
        FileInfo testFileInfo = new FileInfo()
                .setName("test.txt")
                .setContentType("plain/text");

        try (InputStream testFile = getClass().getClassLoader().getResourceAsStream("testFile.text")) {
            FileInfo savedFileInfo = filesRepository.save(testFileInfo, testFile);

            assertNotNull(savedFileInfo);
            assertFalse(StringUtils.isBlank(savedFileInfo.getId()));
            assertNotNull(savedFileInfo.getUploadDateTime());
            assertNotNull(savedFileInfo.getSize());

            Optional<FileInfo> optionalFileInfoFromDB = MongoUtils.findById(mongoOperations, savedFileInfo.getId(), FileInfo.class);
            assertTrue(optionalFileInfoFromDB.isPresent());
            assertEquals(savedFileInfo, optionalFileInfoFromDB.get());
        }
    }

    @Test
    public void getFileInfo() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    public void getFile() throws Exception {
        fail("Not yet implemented");
    }

    @Test
    public void delete() throws Exception {
        fail("Not yet implemented");
    }

    @Configuration
    @EnableAutoConfiguration
    @Import(RepositoriesConfig.class)
    public static class IFilesRepositoryTestConfig extends AbstractMongoConfiguration {
        private MongodForTestsFactory factory;

        @Override
        protected String getDatabaseName() {
            return "test-db";
        }

        @Override
        @Primary
        public Mongo mongo() throws Exception {
            factory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
            return factory.newMongo();
        }

        @PreDestroy
        public void stopMongo() {
            if (Objects.nonNull(factory)) factory.shutdown();
        }
    }
}
