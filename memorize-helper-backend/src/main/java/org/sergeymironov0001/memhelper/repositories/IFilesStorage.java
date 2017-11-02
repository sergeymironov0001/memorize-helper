package org.sergeymironov0001.memhelper.repositories;

import org.sergeymironov0001.memhelper.domain.FileInfo;

import java.io.InputStream;
import java.util.Collection;

public interface IFilesStorage {

    FileInfo store(FileInfo fileInfo, InputStream file);

    FileInfo getFileInfo(String id);

    InputStream getFile(String id);

    void delete(String id);

    void delete(Collection<String> ids);
}
