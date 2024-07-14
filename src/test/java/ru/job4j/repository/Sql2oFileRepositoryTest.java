package ru.job4j.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.File;

import java.util.Optional;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository sql2oFileRepository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void deleteAllFromTable() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE from files").executeUpdate();
        }
    }

    @Test
    public void whenSaveAndFind() {
        var file = sql2oFileRepository.save(new File("file", "file"));
        var savedFile = sql2oFileRepository.findById(file.getId()).get();
        assertThat(savedFile).usingRecursiveComparison().isEqualTo(file);
    }

    @Test
    public void whenFindNotExistUserThenGetEmptyOptional() {
        var file = sql2oFileRepository.findById(0);
        assertThat(file).isEqualTo(Optional.empty());
    }

    @Test
    public void whenDeleteAndFindThenGetEmptyOptional() {
        var file = sql2oFileRepository.save(new File("file", "file"));
        sql2oFileRepository.deleteById(file.getId());
        var savedFile = sql2oFileRepository.findById(file.getId());
        assertThat(savedFile).isEqualTo(empty());
    }
}