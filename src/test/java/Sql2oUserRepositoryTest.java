import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.repository.Sql2oUserRepository;

import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetTheSame() {
        var user = sql2oUserRepository.save(new User(1, "e-mail", "password"));
        var savedUser = sql2oUserRepository.findUserByEmail("e-mail");
        assertThat(user).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    public void findByEmailAndPasswordIsOk() {
        var user = sql2oUserRepository.save(new User(1, "e-mail", "password"));
        var emailRsl = sql2oUserRepository.findUserByEmail("e-mail").get().getEmail();
        var passwordRsl = sql2oUserRepository.findByEmailAndPassword("e-mail", "password").get().getPassword();
        assertThat(emailRsl).isEqualTo(user.get().getEmail());
        assertThat(passwordRsl).isEqualTo(user.get().getPassword());
    }

    @Test
    public void doNotAddSameEmail() {
        var user1 = new User(1, "e-mail", "password");
        var user2 = new User(2, "e-mail", "password2");
        sql2oUserRepository.save(user1);
        assertThat(sql2oUserRepository.save(user2)).isEmpty();
    }

}