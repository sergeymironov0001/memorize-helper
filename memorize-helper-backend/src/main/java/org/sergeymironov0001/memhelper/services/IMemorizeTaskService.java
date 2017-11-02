package org.sergeymironov0001.memhelper.services;

import org.sergeymironov0001.memhelper.domain.File;
import org.sergeymironov0001.memhelper.domain.MemorizeTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface IMemorizeTaskService {

    MemorizeTask getTask(String id);

    Page<MemorizeTask> getTasks(Pageable pageable);

    MemorizeTask createTask(MemorizeTask memorizeTask, List<File> relatedFiles) throws IOException;

    MemorizeTask updateTask(MemorizeTask memorizeTask, List<File> relatedFiles) throws IOException;

    MemorizeTask patchTask(MemorizeTask memorizeTask, List<File> relatedFiles);

    void deleteTask(String id);
}
