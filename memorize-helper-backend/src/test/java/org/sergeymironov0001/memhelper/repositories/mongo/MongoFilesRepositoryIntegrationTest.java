package org.sergeymironov0001.memhelper.repositories.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sergeymironov0001.memhelper.configs.RepositoriesConfig;
import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.sergeymironov0001.memhelper.repositories.FileNotFoundException;
import org.sergeymironov0001.memhelper.repositories.IFilesRepository;
import org.sergeymironov0001.memhelper.testutils.FileTestUtils;
import org.sergeymironov0001.memhelper.testutils.MongoTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoFilesRepositoryIntegrationTest.IFilesRepositoryTestConfig.class})
public class MongoFilesRepositoryIntegrationTest {

    @Autowired
    private IFilesRepository filesRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Test
    public void saveSavesFileCorrectly() throws Exception {
        FileInfo testFileInfo = new FileInfo()
                .setName("test.txt")
                .setContentType("plain/text");

        try (InputStream testFile = getClass().getClassLoader().getResourceAsStream("testFile.txt")) {
            FileInfo savedFileInfo = filesRepository.save(testFileInfo, testFile);

            assertNotNull(savedFileInfo);
            assertFalse(StringUtils.isBlank(savedFileInfo.getId()));
            assertNotNull(savedFileInfo.getUploadDateTime());
            assertNotNull(savedFileInfo.getSize());

            Optional<FileInfo> optionalFileInfoFromDB = MongoTestUtils.findById(mongoOperations,
                    savedFileInfo.getId(), FileInfo.class);
            assertTrue(optionalFileInfoFromDB.isPresent());
            assertEquals(savedFileInfo, optionalFileInfoFromDB.get());
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void getFileInfoThrowsFileNotFoundExceptionIfFileInfoWasNotFound() throws Exception {
        filesRepository.getFileInfo("not-exists-file");
    }

    @Test
    public void getFileInfoReturnsCorrectFileInfo() {
        FileInfo testFileInfo = new FileInfo()
                .setName("testFile.txt")
                .setContentType("plain/text")
                .setSize(12L)
                .setUploadDateTime(LocalDateTime.now());

        testFileInfo = MongoTestUtils.save(mongoOperations, testFileInfo);
        FileInfo savedFileInfo = filesRepository.getFileInfo(testFileInfo.getId());
        assertEquals(testFileInfo, savedFileInfo);
    }

    @Test(expected = FileNotFoundException.class)
    public void getFileThrowsFileNotFoundExceptionIfFileWasNotFound() throws Exception {
        filesRepository.getFile("not-exists-file");
    }

    @Test
    public void getFileReturnsCorrectFile() throws Exception {
        String savedFileId = null;
        String testFileName = "testFile.txt";
        try (InputStream testFile = FileTestUtils.getResourceFileAsStream(testFileName)) {
            savedFileId = MongoTestUtils.saveFile(gridFsTemplate, testFile, testFileName, "plain/text");
        }
        try (InputStream fileFromDB = filesRepository.getFile(savedFileId)) {
            assertTrue(FileTestUtils.contentEquals(
                    FileTestUtils.getResourceFile(testFileName).toFile(),
                    fileFromDB));
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void deleteThrowsFileNotFoundExceptionIfFileInfoWasNotFound() throws Exception {
        filesRepository.delete("not-exists-file");
    }

    @Test(expected = FileNotFoundException.class)
    public void deleteThrowsFileNotFoundExceptionIfFileWasNotFound() throws Exception {
        FileInfo testFileInfo = new FileInfo()
                .setName("testFile.txt")
                .setContentType("plain/text")
                .setSize(12L)
                .setUploadDateTime(LocalDateTime.now());

        testFileInfo = MongoTestUtils.save(mongoOperations, testFileInfo);
        filesRepository.delete(testFileInfo.getId());
    }

    @Test
    public void deleteDeletesFileCorrectly() throws Exception {
        String testFileName = "testFile.txt";
        String savedFileId = null;
        try (InputStream testFile = FileTestUtils.getResourceFileAsStream(testFileName)) {
            savedFileId = MongoTestUtils.saveFile(gridFsTemplate, testFile, testFileName, "plain/text");

            FileInfo testFileInfo = new FileInfo()
                    .setId(savedFileId)
                    .setName("testFile.txt")
                    .setContentType("plain/text")
                    .setSize(12L)
                    .setUploadDateTime(LocalDateTime.now());

            MongoTestUtils.save(mongoOperations, testFileInfo);

            assertTrue(MongoTestUtils.findById(mongoOperations, savedFileId, FileInfo.class).isPresent());
            assertThat(MongoTestUtils.getFile(gridFsTemplate, savedFileId), is(not(nullValue())));
        }

        filesRepository.delete(savedFileId);

        Optional<FileInfo> fileInfoOptional = MongoTestUtils.findById(mongoOperations, savedFileId, FileInfo.class);
        assertFalse(fileInfoOptional.isPresent());
        assertThat(MongoTestUtils.getFile(gridFsTemplate, savedFileId), is(nullValue()));
    }

    @Configuration
    @EnableAutoConfiguration
    @Import(RepositoriesConfig.class)
    public static class IFilesRepositoryTestConfig extends AbstractMongoConfiguration {
        private static final String HOST = "localhost";
        private static final int PORT = 27017;
        private static final String DB_NAME = "test-db";

        private static final MongodStarter starter = MongodStarter.getDefaultInstance();
        private MongodExecutable mongodExecutable;
        private MongodProcess mongoD;

        @Override
        protected String getDatabaseName() {
            return DB_NAME;
        }

        @PostConstruct
        public void init() throws IOException {
            mongodExecutable = starter.prepare(new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                    .build());
            mongoD = mongodExecutable.start();
        }

        @Override
        @Primary
        public Mongo mongo() throws Exception {
            return new MongoClient(HOST, PORT);
        }

        @PreDestroy
        public void stopMongo() {
            mongoD.stop();
            mongodExecutable.stop();
        }
    }
}
