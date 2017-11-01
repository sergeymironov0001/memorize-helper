package org.sergeymironov0001.memhelper.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@Accessors(fluent = true)
public class MemorizeTask implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private LocalDate startDate;

    private List<String> tags;

    private List<Repeat> repeats;

    private List<FileInfo> relatedFile = new ArrayList<>();

    private List<String> relatedUrls;
}
