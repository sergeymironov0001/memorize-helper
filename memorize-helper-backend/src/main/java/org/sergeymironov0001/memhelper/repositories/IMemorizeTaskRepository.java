package org.sergeymironov0001.memhelper.repositories;

import org.sergeymironov0001.memhelper.domain.MemorizeTask;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IMemorizeTaskRepository extends PagingAndSortingRepository<MemorizeTask, String> {
}
