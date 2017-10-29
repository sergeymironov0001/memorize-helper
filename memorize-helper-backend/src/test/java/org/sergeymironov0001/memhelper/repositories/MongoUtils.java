package org.sergeymironov0001.memhelper.repositories;

import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class MongoUtils {

    public <T> Optional<T> findById(MongoOperations mongoOperations, String id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(id));
        List<T> items = mongoOperations.find(query, clazz);
        return items.stream().findFirst();
    }
}
