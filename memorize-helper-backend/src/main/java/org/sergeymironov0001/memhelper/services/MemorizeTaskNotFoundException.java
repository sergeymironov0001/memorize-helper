package org.sergeymironov0001.memhelper.services;

import lombok.Getter;

@Getter
public class MemorizeTaskNotFoundException extends RuntimeException {
    private final String id;

    public MemorizeTaskNotFoundException(String id) {
        super(String.format("Memorize task with id \'%s\' was not found", id));
        this.id = id;
    }
}
