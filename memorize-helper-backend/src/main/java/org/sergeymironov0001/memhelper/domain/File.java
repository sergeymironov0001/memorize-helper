package org.sergeymironov0001.memhelper.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Data
@Accessors(fluent = true)
@RequiredArgsConstructor
public class File {
    private final FileInfo info;
    private final InputStream content;

    public static File fromMultipartFile(MultipartFile multipartFile) throws IOException {
        FileInfo info = new FileInfo()
                .setName(multipartFile.getOriginalFilename())
                .setContentType(multipartFile.getContentType());

        return new File(info, multipartFile.getInputStream());
    }
}