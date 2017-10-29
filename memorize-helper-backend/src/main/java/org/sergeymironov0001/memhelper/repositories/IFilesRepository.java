package org.sergeymironov0001.memhelper.repositories;

import org.sergeymironov0001.memhelper.domain.FileInfo;

import java.io.InputStream;

public interface IFilesRepository {

    FileInfo save(FileInfo fileInfo, InputStream file);

    FileInfo getFileInfo(String id);

    InputStream getFile(String id);

    void delete(String id);
}
