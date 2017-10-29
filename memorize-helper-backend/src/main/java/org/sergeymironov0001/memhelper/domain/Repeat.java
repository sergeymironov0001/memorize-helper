package org.sergeymironov0001.memhelper.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
public class Repeat {
    @Id
    private String id;

    private String taskId;

    private LocalDate date;

    private RepeatStatus status;
}
