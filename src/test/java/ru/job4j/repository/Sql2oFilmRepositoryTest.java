package ru.job4j.repository;

import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.Film;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository sql2oFilmRepository;
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
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
    }

    @BeforeEach
    public void insertTables() {
        try (var connection = sql2o.open()) {
            connection.createQuery("""
                 INSERT INTO genres (id, name)
                 VALUES (1, 'genre')
                 """).executeUpdate();
            connection.createQuery("""
                 INSERT INTO files (id, name, path)
                 VALUES (1, 'file', 'path')
                 """).executeUpdate();
            connection.createQuery("""
                 INSERT INTO films (id, name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                 VALUES (1, 'film1', 'film1', 2000, 1, 18, 60, 1),
                        (2, 'film2', 'film2', 2001, 1, 18, 120, 1),
                        (3, 'film3', 'film3', 2002, 1, 18, 180, 1)
                 """).executeUpdate();
        }
    }


    @AfterEach
    public void deleteAllFromTables() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM films").executeUpdate();
            connection.createQuery("DELETE FROM genres").executeUpdate();
            connection.createQuery("DELETE FROM files").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenSuccess() {
        var expectedFilm = new Film(1, "film1", "film1", 2000, 1, 18, 60, 1);
        var actualFilm = sql2oFilmRepository.findById(1).get();
        assertThat(actualFilm).usingRecursiveComparison().isEqualTo(expectedFilm);
    }

    @Test
    public void whenFindByIdNotExistFilmThenGetEmptyOptional() {
        var actualFilm = sql2oFilmRepository.findById(0);
        assertThat(actualFilm).isEmpty();
    }

    @Test
    public void whenFindAllThenSuccess() {
        var film1 = new Film(1, "film1", "film1", 2000, 1, 18, 60, 1);
        var film2 = new Film(2, "film2", "film2", 2001, 1, 18, 120, 1);
        var film3 = new Film(3, "film3", "film3", 2002, 1, 18, 180, 1);
        var expectedFilms = List.of(film1, film2, film3);
        var actualFilms = sql2oFilmRepository.findAll();
        assertThat(actualFilms).usingRecursiveComparison().isEqualTo(expectedFilms);
    }

    @Test
    public void whenFindAllOnEmptyTableThenGetEmptyCollection() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM films").executeUpdate();
        }

        var actualFilms = sql2oFilmRepository.findAll();
        assertThat(actualFilms).isEmpty();
    }
}