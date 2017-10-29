package org.sergeymironov0001.memhelper.repositories;

import lombok.Getter;

@Getter
public class FileNotFoundException extends RuntimeException {

    private final String id;

    public FileNotFoundException(final String id) {
        super(String.format("File with id: \'%s\' was not found", id));
        this.id = id;
    }
}
