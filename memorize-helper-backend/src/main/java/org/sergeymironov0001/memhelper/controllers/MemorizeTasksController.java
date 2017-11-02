package org.sergeymironov0001.memhelper.controllers;

import lombok.RequiredArgsConstructor;
import org.sergeymironov0001.memhelper.domain.File;
import org.sergeymironov0001.memhelper.domain.MemorizeTask;
import org.sergeymironov0001.memhelper.services.IMemorizeTaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ResponseBody
@RequiredArgsConstructor
@RequestMapping(path = "/tasks")
public class MemorizeTasksController {

    private final IMemorizeTaskService memorizeTaskService;

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemorizeTask getTask(@RequestParam String id) {
        return memorizeTaskService.getTask(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<MemorizeTask> getTasks(Pageable pageable) {
        return memorizeTaskService.getTasks(pageable);
    }

    @PostMapping(path = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemorizeTask createTask(
            @RequestPart(value = "task") MemorizeTask memorizeTask,
            @RequestPart(value = "file", required = false) MultipartFile[] relatedFiles)
            throws IOException {

        return memorizeTaskService.createTask(memorizeTask, convertToFiles(relatedFiles));
    }

    @PostMapping(path = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemorizeTask updateTask(@PathVariable String id,
                                   @RequestPart("task") MemorizeTask memorizeTask,
                                   @RequestPart(value = "file", required = false) MultipartFile[] relatedFiles)
            throws IOException {

        memorizeTask.setId(id);
        return memorizeTaskService.updateTask(memorizeTask, convertToFiles(relatedFiles));
    }

    @PatchMapping(path = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemorizeTask patchTask(@PathVariable String id,
                                  @RequestPart("task") MemorizeTask memorizeTask,
                                  @RequestPart(value = "file", required = false) MultipartFile[] relatedFiles)
            throws IOException {
        memorizeTask.setId(id);
        return memorizeTaskService.patchTask(memorizeTask, convertToFiles(relatedFiles));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTask(@RequestParam String id) {
        memorizeTaskService.deleteTask(id);
    }

    private static List<File> convertToFiles(MultipartFile[] multipartFiles) throws IOException {
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            files.add(File.fromMultipartFile(multipartFile));
        }
        return files;
    }
}
