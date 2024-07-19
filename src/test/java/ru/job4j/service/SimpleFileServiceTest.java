package ru.job4j.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.job4j.dto.FileDto;
import ru.job4j.model.File;
import ru.job4j.repository.FileRepository;
import java.nio.file.Path;
import java.nio.file.Paths;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFileServiceTest {
    private FileRepository fileRepository;
    private FileService fileService;
    private String directory;

    @BeforeEach
    public void initService(@TempDir Path tempDir) {
        fileRepository = mock(FileRepository.class);
        fileService = new SimpleFileService(fileRepository, tempDir.toString());
        directory = tempDir.toString();
    }

    @Test
    public void whenSaveThenReturnFile() {
        FileDto fileDto = new FileDto("file.txt", new byte[] {1, 2, 3});
        File expectedFile = new File(fileDto.getName(), Paths.get(directory, "file.txt").toString());
        when(fileRepository.save(any())).thenReturn(expectedFile);

        File actualFile = fileService.save(fileDto);
        assertThat(actualFile).usingRecursiveComparison().isEqualTo(expectedFile);
    }
}