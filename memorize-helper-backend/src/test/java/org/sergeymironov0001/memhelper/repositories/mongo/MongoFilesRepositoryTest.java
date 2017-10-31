package org.sergeymironov0001.memhelper.repositories.mongo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.sergeymironov0001.memhelper.testutils.FileTestUtils;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.InputStream;

public class MongoFilesRepositoryTest {
    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private IMongoFileInfoRepository mongoFileInfoRepository;

    private MongoFilesRepository mongoFilesRepository;

    @Before
    public void init() {
        mongoFilesRepository = new MongoFilesRepository(gridFsTemplate, mongoFileInfoRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveThrowsIllegalArgumentExceptionIfFileInfoIsNull() throws Exception {
        try (InputStream testFile = FileTestUtils.getResourceFileAsStream("testFile.txt")) {
            mongoFilesRepository.save(null, testFile);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveThrowsIllegalArgumentExceptionIfInputStreamIsNull() throws Exception {
        mongoFilesRepository.save(new FileInfo(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFileInfoThrowsIllegalArgumentExceptionIfIdIsNull() throws Exception {
        mongoFilesRepository.getFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFileThrowsIllegalArgumentExceptionIfIdIsNull() throws Exception {
        mongoFilesRepository.getFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteThrowsIllegalArgumentExceptionIfIdIsNull() throws Exception {
        mongoFilesRepository.delete(null);
    }

}