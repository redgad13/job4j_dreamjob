package ru.job4j.dreamjob.repository;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Primary
@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;
    private static final Logger logger = Logger.getLogger(Sql2oUserRepository.class.getName());

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        Optional<User> rsl;
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO users (email, password)
                    VALUES (:email, :password)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            rsl = Optional.of(user);
        } catch (Sql2oException e) {
            logger.log(Level.ALL, "Exception occurred");
            rsl = Optional.empty();
        }
        return rsl;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var sql = "SELECT * FROM users WHERE email = :email AND password = :password";
            var query = connection.createQuery(sql);
            query.addParameter("email", email)
                    .addParameter("password", password);
            var user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        try (var connection = sql2o.open()) {
            var sql = "SELECT * FROM users WHERE email = :email";
            var query = connection.createQuery(sql);
            query.addParameter("email", email);
            var user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            return query.executeAndFetch(User.class);
        }
    }

    @Override
    public boolean deleteById(int id) {
        boolean rsl;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id = :id");
            query.addParameter("id", id);
            rsl = query.executeUpdate().getResult() > 0;
        }
        return rsl;
    }
}
