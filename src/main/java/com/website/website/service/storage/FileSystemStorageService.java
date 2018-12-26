package com.website.website.service.storage;

import com.website.website.domain.File;
import com.website.website.domain.Task;
import com.website.website.domain.User;
import com.website.website.repo.FilesRepo;
import com.website.website.repo.TaskRepo;
import com.website.website.repo.UserRepo;
import com.website.website.service.notes.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private String[] formats = {"jpg", "png", "gif", "jpeg", "JPG", "PNG", "GIF", "JPEG"};
    private String fileRequest = "/img/";

    @Autowired
    private FilesRepo filesRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Value("${max.filesize}")
    int MAX_FILE_SIZE;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public boolean rightFormat(String fileName) {
        for (String format:formats) {
            if (fileName.length() - fileName.lastIndexOf(format) <= 4) {
                return true;
            }
        }
        return false;
    }

    public void delete(String fileName) {
        if(!fileName.isEmpty()) {
            Path path = Paths.get(rootLocation + "/" + fileName.replace(fileRequest, ""));
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String store(MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        String filename = uuidFile + '.' + StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new StorageException("File is too big");
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                            StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return fileRequest + filename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public int getFilesNumber(User user) {
        int numb = 0;
        List<Task> tasks = taskRepo.findAllByUserOrderByDate(user);
        for (Task task: tasks) {
            List<File> files = filesRepo.findAllByTask(task);
            numb += files.size();
        }
        return numb;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}