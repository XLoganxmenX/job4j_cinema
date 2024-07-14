package ru.job4j.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.model.User;

import java.util.Optional;

@Repository
public class Sql2oUserRepository implements UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oUserRepository.class);
    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        Optional<User> returnUser = Optional.empty();
        try (var connection = sql2o.open()) {
            String sqlInsert = """
                      INSERT INTO users (full_name, email, password)
                      VALUES (:full_name, :email, :password)
                      """;
            var query = connection.createQuery(sqlInsert, true)
                    .addParameter("full_name", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            var execution = query.executeUpdate();
            int generatedId = execution.getKey(Integer.class);
            user.setId(generatedId);
            returnUser = Optional.of(user);
        } catch (Sql2oException e) {
            LOGGER.info("Попытка сохранения существующего пользователя", e);
        }
        return returnUser;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            String sqlSelect = """
                    SELECT * FROM users
                    WHERE email = :email AND password = :password
                    """;
            var query = connection.createQuery(sqlSelect)
                    .addParameter("email", email)
                    .addParameter("password", password);
            User user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }
}
