package ru.job4j.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.Hall;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oHallRepositoryTest {
    private static Sql2oHallRepository sql2oHallRepository;
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
        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    @AfterEach
    public void deleteAllFromTable() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE from halls").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenGetGenre() {
        Hall expectHall = new Hall(1, "RED", 10, 10, "RED");
        try (var connection = sql2o.open()) {
            connection.createQuery("""
                            INSERT INTO halls(id, name, row_count, place_count, description)
                            VALUES (:id, :name, :row_count, :place_count, :description)
                            """)
                    .addParameter("id", expectHall.getId())
                    .addParameter("name", expectHall.getName())
                    .addParameter("row_count", expectHall.getRowCount())
                    .addParameter("place_count", expectHall.getPlaceCount())
                    .addParameter("description", expectHall.getDescription())
                    .executeUpdate();
        }

        Hall actualHall = sql2oHallRepository.findById(1).get();
        assertThat(actualHall).usingRecursiveComparison().isEqualTo(expectHall);
    }

    @Test
    public void whenFindByIdNotExistGenreThenGetEmptyOptional() {
        Optional<Hall> actualHall = sql2oHallRepository.findById(1);
        assertThat(actualHall).isEmpty();
    }
}