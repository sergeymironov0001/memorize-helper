package org.sergeymironov0001.memhelper.controllers;

import lombok.RequiredArgsConstructor;
import org.sergeymironov0001.memhelper.domain.FileInfo;
import org.sergeymironov0001.memhelper.repositories.IFilesStorage;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;


@ResponseBody
@RequestMapping(path = "/files")
@RequiredArgsConstructor
public class FilesStorageController {

    private final IFilesStorage filesRepository;

//    @PostMapping(
//            path = "/",
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public FileInfo uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        FileInfo fileInfo = new FileInfo()
//                .setName(file.getOriginalFilename())
//                .setContentType(file.getContentType());
//        try (InputStream inputStream = file.getInputStream()) {
//            return filesRepository.save(fileInfo, inputStream);
//        }
//    }

    @GetMapping(path = "/{id}/info",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FileInfo getFileInfo(@PathVariable String id) {
        return filesRepository.getFileInfo(id);
    }

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String id) {
        FileInfo fileInfo = filesRepository.getFileInfo(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentDispositionFormData("attachment", fileInfo.getName());
        responseHeaders.setContentType(MediaType.parseMediaType(fileInfo.getContentType()));
        responseHeaders.setContentLength(fileInfo.getSize());

        InputStream inputStream = filesRepository.getFile(id);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteFile(@PathVariable String id) {
        filesRepository.delete(id);
    }
}


