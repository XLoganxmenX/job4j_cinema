package ru.job4j.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.Genre;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oGenreRepositoryTest {
    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oGenreRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @AfterEach
    public void deleteAllFromTable() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE from genres").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenGetGenre() {
        Genre expectGenre = new Genre(1, "fantasy");
        try (var connection = sql2o.open()) {
            connection.createQuery("INSERT INTO genres(id, name) VALUES (:id, :name)")
                    .addParameter("id", expectGenre.getId())
                    .addParameter("name", expectGenre.getName())
                    .executeUpdate();
        }

        Genre actualGenre = sql2oGenreRepository.findById(1).get();
        assertThat(actualGenre).isEqualTo(expectGenre);
    }

    @Test
    public void whenFindByIdNotExistGenreThenGetEmptyOptional() {
        Optional<Genre> actualGenre = sql2oGenreRepository.findById(1);
        assertThat(actualGenre).isEmpty();
    }
}