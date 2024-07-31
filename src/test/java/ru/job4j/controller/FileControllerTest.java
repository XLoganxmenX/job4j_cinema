package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dto.FileDto;
import ru.job4j.service.FileService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {
    private FileService fileService;
    private FileController fileController;
    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
        testFile = new MockMultipartFile("testFile.img", new byte[] {1, 2, 3});
    }

    @Test
    public void whenGetByIdIsSuccess() throws IOException {
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        int fileId = 1;
        when(fileService.getFileById(fileId)).thenReturn(Optional.of(fileDto));

        var response = fileController.getById(fileId);

        assertThat(response).isEqualTo(ResponseEntity.ok(fileDto.getContent()));
    }

    @Test
    public void whenGetByIdInvalid() {
        int fileId = 1;
        when(fileService.getFileById(fileId)).thenReturn(Optional.empty());

        var response = fileController.getById(fileId);

        assertThat(response).isEqualTo(ResponseEntity.notFound().build());
    }

}