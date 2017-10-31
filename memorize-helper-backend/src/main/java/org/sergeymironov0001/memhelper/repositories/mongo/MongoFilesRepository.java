package org.sergeymironov0001.memhelper.repositories.mongo;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.sergeymironov0001.memhelper.repositories.FileNotFoundException;
import org.sergeymironov0001.memhelper.repositories.IFilesRepository;
import org.sergeymironov0001.memhelper.utils.DateUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.InputStream;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class MongoFilesRepository implements IFilesRepository {

    private final GridFsTemplate gridFsTemplate;

    private final IMongoFileInfoRepository fileInfoRepository;

    @Override
    public FileInfo save(FileInfo fileInfo, InputStream file) {
        if (Objects.isNull(fileInfo)) {
            throw new IllegalArgumentException("File info can't be null to store file");
        }
        if (Objects.isNull(file)) {
            throw new IllegalArgumentException("File can't be null to store file");
        }

        GridFSFile gridFSFile = gridFsTemplate.store(file, fileInfo.getName(), fileInfo.getContentType());

        fileInfo.setId(gridFSFile.getId().toString())
                .setUploadDateTime(DateUtils.dateToLocalDateTime(gridFSFile.getUploadDate()))
                .setSize(gridFSFile.getLength());

        return fileInfoRepository.save(fileInfo);
    }

    @Override
    public FileInfo getFileInfo(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Id can't be blank to get file info ");
        }
        FileInfo fileInfo = fileInfoRepository.findOne(id);
        if (Objects.isNull(fileInfo)) {
            throw new FileNotFoundException(id);
        }
        return fileInfo;
    }

    @Override
    public InputStream getFile(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Id can't be blank to get file");
        }
        Query findFileQuery = new Query(GridFsCriteria.where("_id").is(id));
        GridFSDBFile file = gridFsTemplate.findOne(findFileQuery);

        if (Objects.isNull(file)) {
            throw new FileNotFoundException(id);
        }
        return file.getInputStream();
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Id can't be blank to delete file");
        }
        // To check exists file info or not
        getFileInfo(id);
        fileInfoRepository.delete(id);

        // To check exists file or not
        getFile(id);
        gridFsTemplate.delete(new Query(GridFsCriteria.where("_id").is(id)));
    }
}
