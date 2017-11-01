package org.sergeymironov0001.memhelper.repositories.mongo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.sergeymironov0001.memhelper.testutils.FileTestUtils;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.InputStream;

public class MongoFilesStorageTest {

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private IMongoFileInfoRepository mongoFileInfoRepository;

    private MongoFilesStorage mongoFilesStorage;

    @Before
    public void init() {
        mongoFilesStorage = new MongoFilesStorage(gridFsTemplate, mongoFileInfoRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveThrowsIllegalArgumentExceptionIfFileInfoIsNull() throws Exception {
        try (InputStream testFile = FileTestUtils.getResourceFileAsStream("testFile.txt")) {
            mongoFilesStorage.save(null, testFile);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveThrowsIllegalArgumentExceptionIfInputStreamIsNull() throws Exception {
        mongoFilesStorage.save(new FileInfo(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFileInfoThrowsIllegalArgumentExceptionIfIdIsNull() throws Exception {
        mongoFilesStorage.getFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFileThrowsIllegalArgumentExceptionIfIdIsNull() throws Exception {
        mongoFilesStorage.getFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteThrowsIllegalArgumentExceptionIfIdIsNull() throws Exception {
        mongoFilesStorage.delete(null);
    }

}