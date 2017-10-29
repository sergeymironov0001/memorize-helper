package org.sergeymironov0001.memhelper.repositories.mongo;

import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IMongoFileInfoRepository extends PagingAndSortingRepository<FileInfo, String> {
}
