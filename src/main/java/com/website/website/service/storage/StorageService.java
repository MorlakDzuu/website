package com.website.website.service.storage;

import com.website.website.domain.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    void delete(String fileName);

    boolean rightFormat(String fileName);

    Stream<Path> loadAll();

    Path load(String filename);

    int getFilesNumber(User user);

    Resource loadAsResource(String filename);

}