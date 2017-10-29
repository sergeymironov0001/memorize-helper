package org.sergeymironov0001.memhelper.services;

import lombok.RequiredArgsConstructor;
import org.sergeymironov0001.memhelper.domain.MemorizeTask;
import org.sergeymironov0001.memhelper.repositories.IMemorizeTaskRepository;

import java.util.List;


@RequiredArgsConstructor
public class MemorizeTaskService implements IMemorizeTaskService {

    private final IMemorizeTaskRepository memorizeTaskRepository;

    @Override
    public MemorizeTask getTask(String id) {
        return null;
    }

    @Override
    public List<MemorizeTask> getTasks(List<String> ids) {
        return null;
    }

    @Override
    public MemorizeTask saveTask(MemorizeTask memorizeTask) {
        return null;
    }

    @Override
    public MemorizeTask deleteTask(String id) {
        return null;
    }
}
