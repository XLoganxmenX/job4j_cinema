package ru.job4j.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.FilmSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;


class Sql2oFilmSessionRepositoryTest {
    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;
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
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
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
                 VALUES (1, 'film1', 'film1', 2000, 1, 18, 60, 1)
                 """).executeUpdate();
            connection.createQuery("""
                 INSERT INTO halls (id, name, row_count, place_count, description)
                 VALUES (1, 'RED', 10, 10, 'RED')
                 """).executeUpdate();
            connection.createQuery("""
                 INSERT INTO film_sessions (id, film_id, halls_id, start_time, end_time, price)
                 VALUES (1, 1, 1, '2000-01-01 00:00:00', '2000-01-01 01:00:00', 100),
                        (2, 1, 1, '2000-01-02 00:00:00', '2000-01-02 02:00:00', 200),
                        (3, 1, 1, '2000-01-03 00:00:00', '2000-01-03 03:00:00', 300)
                 """).executeUpdate();
        }
    }

    @AfterEach
    public void deleteAllFromTables() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM film_sessions").executeUpdate();
            connection.createQuery("DELETE FROM halls").executeUpdate();
            connection.createQuery("DELETE FROM films").executeUpdate();
            connection.createQuery("DELETE FROM genres").executeUpdate();
            connection.createQuery("DELETE FROM files").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenSuccess() {
        var date = LocalDateTime.parse("2000-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        var expectedFilmSession = new FilmSession(1, 1, 1, date, date.plusMinutes(60), 100);
        var actualFilmSession = sql2oFilmSessionRepository.findById(1).get();
        assertThat(actualFilmSession).usingRecursiveComparison().isEqualTo(expectedFilmSession);
    }

    @Test
    public void whenFindByIdNotExistFilmThenGetEmptyOptional() {
        var actualFilmSession = sql2oFilmSessionRepository.findById(0);
        assertThat(actualFilmSession).isEmpty();
    }

    @Test
    public void whenFindAllThenSuccess() {
        var date = LocalDateTime.parse("2000-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        var filmSession1 = new FilmSession(1, 1, 1, date, date.plusMinutes(60), 100);
        var filmSession2 = new FilmSession(2, 1, 1, date.plusDays(1), date.plusDays(1).plusMinutes(120), 200);
        var filmSession3 = new FilmSession(3, 1, 1, date.plusDays(2), date.plusDays(2).plusMinutes(180), 300);
        var expectedFilmSessions = List.of(filmSession1, filmSession2, filmSession3);
        var actualFilmSessions = sql2oFilmSessionRepository.findAll();
        assertThat(actualFilmSessions).usingRecursiveComparison().isEqualTo(expectedFilmSessions);
    }
}