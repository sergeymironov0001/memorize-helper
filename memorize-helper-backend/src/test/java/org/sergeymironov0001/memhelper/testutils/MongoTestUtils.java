package org.sergeymironov0001.memhelper.testutils;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class MongoTestUtils {

    public <T> Optional<T> findById(MongoOperations mongoOperations, String id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(id));
        List<T> items = mongoOperations.find(query, clazz);
        return items.stream().findFirst();
    }

    public <T> T save(MongoOperations mongoOperations, T document) {
        mongoOperations.save(document);
        return document;
    }

    public String saveFile(GridFsTemplate gridFsTemplate,
                           InputStream inputStream,
                           String fileName,
                           String contentType) {
        GridFSFile gridFSFile = gridFsTemplate.store(inputStream, fileName, contentType);
        return gridFSFile.getId().toString();
    }

    public InputStream getFile(GridFsTemplate gridFsTemplate, String fileId) {
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(new Query(GridFsCriteria.where("_id").is(fileId)));
        if (Objects.isNull(gridFSDBFile)) return null;
        return gridFSDBFile.getInputStream();
    }
}
