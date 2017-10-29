package org.sergeymironov0001.memhelper.services;

import org.sergeymironov0001.memhelper.domain.MemorizeTask;

import java.util.List;

public interface IMemorizeTaskService {

    MemorizeTask getTask(String id);

    List<MemorizeTask> getTasks(List<String> ids);

    MemorizeTask saveTask(MemorizeTask memorizeTask);

    MemorizeTask deleteTask(String id);
}
