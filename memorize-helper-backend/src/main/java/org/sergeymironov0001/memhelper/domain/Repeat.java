package org.sergeymironov0001.memhelper.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Document
@Accessors(fluent = true)
public class Repeat implements Serializable {

    private LocalDate date;

    private RepeatStatus status;
}
