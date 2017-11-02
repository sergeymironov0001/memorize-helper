package org.sergeymironov0001.memhelper.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.sergeymironov0001.memhelper.domain.File;
import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.sergeymironov0001.memhelper.domain.MemorizeTask;
import org.sergeymironov0001.memhelper.repositories.IFilesStorage;
import org.sergeymironov0001.memhelper.repositories.IMemorizeTaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
public class MemorizeTaskService implements IMemorizeTaskService {

    private final IMemorizeTaskRepository memorizeTaskRepository;

    private final IFilesStorage filesStorage;

    @Override
    public MemorizeTask getTask(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Id can't be blank to get memorize task");
        }
        log.debug("Getting memorize task by id \'{}\'", id);
        MemorizeTask memorizeTask = memorizeTaskRepository.findOne(id);
        if (Objects.isNull(memorizeTask)) {
            throw new MemorizeTaskNotFoundException(id);
        }
        log.debug("Memorize task with id \'{}\' was gotten: {} ", id, memorizeTask);
        return memorizeTask;
    }

    @Override
    public Page<MemorizeTask> getTasks(Pageable pageable) {
        if (Objects.isNull(pageable)) {
            throw new IllegalArgumentException("Pageable can't be null to get tasks");
        }
        log.debug("Getting tasks for page: {}", pageable);
        Page<MemorizeTask> page = memorizeTaskRepository.findAll(pageable);
        log.debug("Tasks for page {} was gotten", pageable);
        return page;
    }

    @Override
    public MemorizeTask createTask(MemorizeTask memorizeTask, List<File> relatedFiles) throws IOException {
        if (Objects.isNull(memorizeTask)) {
            throw new IllegalArgumentException("Memorize task can't be null to create memorize task");
        }
        memorizeTask.setId(null);

        Collection<FileInfo> savedFilesInfo = storeFiles(relatedFiles);
        memorizeTask.getRelatedFiles().addAll(savedFilesInfo);

        log.debug("Creating memorize task: {}", memorizeTask);
        MemorizeTask createdMemorizeTask = memorizeTaskRepository.save(memorizeTask);
        log.debug("Memorize task was created: {}", createdMemorizeTask);
        return createdMemorizeTask;
    }

    @Override
    public MemorizeTask updateTask(MemorizeTask memorizeTask, List<File> relatedFiles) throws IOException {
        if (Objects.isNull(memorizeTask)) {
            throw new IllegalArgumentException("Memorize task can't be null to update memorize task");
        }
        if (StringUtils.isBlank(memorizeTask.getId())) {
            throw new IllegalArgumentException("Memorize task's id can't be null to update memorize task");
        }

        final String memorizeTaskId = memorizeTask.getId();
        MemorizeTask oldVersion = getTask(memorizeTask.getId());

        log.debug("Updating memorize task with id \'{}\'", memorizeTaskId);
        if (!oldVersion.getRelatedFiles().isEmpty()) {
            log.debug("Deleting old related files for the memorize task with id \'{}\'", memorizeTaskId);
            Collection<String> filesToDelete = getRemovedFileIds(oldVersion, memorizeTask);
            filesStorage.delete(filesToDelete);
            log.debug("Old related files to the memorize task with id \'{}\' were deleted", memorizeTaskId);

            List<FileInfo> unchangedFiles = getUnchangedFiles(oldVersion, memorizeTask);
            memorizeTask.setRelatedFiles(unchangedFiles);
        }

        if (Objects.nonNull(relatedFiles) && !relatedFiles.isEmpty()) {
            log.debug("Storing new related files for the memorize task with id \'{}\'", memorizeTaskId);
            Collection<FileInfo> storedFilesInfo = storeFiles(relatedFiles);
            memorizeTask.getRelatedFiles().addAll(storedFilesInfo);
            log.debug("New related files for the memorize task with id \'{}\' were stored", memorizeTaskId);
        }

        MemorizeTask updatedMemorizeTask = memorizeTaskRepository.save(memorizeTask);
        log.debug("Memorize task with id \'{}\' was updated", memorizeTaskId);
        return updatedMemorizeTask;
    }

    @Override
    public MemorizeTask patchTask(MemorizeTask memorizeTask, List<File> relatedFiles) {
//        if (Objects.isNull(memorizeTask)) {
//            throw new IllegalArgumentException("Memorize task can't be null to patch memorize task");
//        }
//        if (StringUtils.isBlank(memorizeTask.id())) {
//            throw new IllegalArgumentException("Memorize task's id can't be null to patch memorize task");
//        }
//        MemorizeTask oldVersion = getTask(memorizeTask.id());
//
//
//        return null;
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteTask(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Id can't be null to delete memorize task");
        }
        log.debug("Deleting memorize task by id \'{}\'", id);
        MemorizeTask taskToDelete = memorizeTaskRepository.findOne(id);
        if (Objects.isNull(taskToDelete)) {
            throw new MemorizeTaskNotFoundException(id);
        }
        memorizeTaskRepository.delete(id);
        log.debug("Memorize task with id \'{}\' was deleted", id);
    }

    private List<FileInfo> getUnchangedFiles(MemorizeTask oldVersion,
                                             MemorizeTask newVersion) {
        Collection<String> oldVersionFileIds = oldVersion.getRelatedFiles().stream()
                .map(FileInfo::getId)
                .collect(Collectors.toList());

        Collection<String> newVersionFileIds = newVersion.getRelatedFiles().stream()
                .map(FileInfo::getId)
                .collect(Collectors.toList());

        Collection<String> unchangedFileIds =
                CollectionUtils.intersection(oldVersionFileIds, newVersionFileIds);

        return newVersion.getRelatedFiles().stream()
                .filter(fi -> unchangedFileIds.contains(fi.getId()))
                .collect(Collectors.toList());
    }

    private Collection<String> getRemovedFileIds(MemorizeTask oldVersion,
                                                 MemorizeTask newVersion) {
        Collection<String> oldVersionFileIds = oldVersion.getRelatedFiles().stream()
                .map(FileInfo::getId)
                .collect(Collectors.toList());

        Collection<String> newVersionFileIds = newVersion.getRelatedFiles().stream()
                .map(FileInfo::getId)
                .collect(Collectors.toList());

        return CollectionUtils.subtract(oldVersionFileIds, newVersionFileIds);
    }

    private Collection<FileInfo> storeFiles(Collection<File> files) throws IOException {
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (File file : files) {
            try (InputStream content = file.content()) {
                fileInfoList.add(filesStorage.store(file.info(), content));
            }
        }
        return fileInfoList;
    }
}
