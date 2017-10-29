package org.sergeymironov0001.memhelper.domain;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Document
@Data
@Accessors(chain = true)
public class FileInfo {
    private String id;

    private String name;

    private Long size;

    private String contentType;

    private LocalDateTime uploadDateTime;

    private Map<String, String> metaInfo = new LinkedHashMap<>();
}
