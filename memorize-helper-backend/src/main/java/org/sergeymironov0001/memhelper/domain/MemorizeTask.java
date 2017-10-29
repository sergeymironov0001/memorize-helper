package org.sergeymironov0001.memhelper.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Document
public class MemorizeTask implements Serializable {
    @Id
    private String id;

    private String name;

    private String description;

    private LocalDate startDate;

    private List<String> tags;

    private List<Repeat> repeats;

    private List<FileInfo> relatedFiles;

    private List<String> relatedUrls;
}
