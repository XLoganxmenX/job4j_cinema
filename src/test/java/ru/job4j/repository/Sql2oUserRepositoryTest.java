package ru.job4j.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.User;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;
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
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void deleteAllFromTable() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE from users").executeUpdate();
        }
    }

    @Test
    public void whenSaveAndFind() {
        var user = sql2oUserRepository.save(
                new User(0, "user", "user@mail.com", "password")
        ).get();
        var savedUser = sql2oUserRepository.findByEmailAndPassword("user@mail.com", "password").get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveExistUserThenGetEmptyOptional() {
        sql2oUserRepository.save(new User(0, "user", "user@mail.com", "password"));

        var savedUser = sql2oUserRepository.save(
                new User(0, "user", "user@mail.com", "password")
        );
        assertThat(savedUser).isEqualTo(Optional.empty());
    }

    @Test
    public void whenFindNotExistUserThenGetEmptyOptional() {
        var user = sql2oUserRepository.findByEmailAndPassword("user@mail.com", "password");
        assertThat(user).isEqualTo(Optional.empty());
    }

    @Test
    public void whenFindByCorrectEmailButInvalidPasswordThenGetEmptyOptional() {
        sql2oUserRepository.save(new User(0, "user", "user@mail.com", "password"));
        var findUser = sql2oUserRepository.findByEmailAndPassword("user@mail.com", "123456789");
        assertThat(findUser).isEqualTo(Optional.empty());
    }

    @Test
    public void whenFindByInvalidEmailButCorrectPasswordThenGetEmptyOptional() {
        sql2oUserRepository.save(new User(0, "user", "user@mail.com", "password"));
        var findUser = sql2oUserRepository.findByEmailAndPassword("invalid@mail.com", "password");
        assertThat(findUser).isEqualTo(Optional.empty());
    }
}