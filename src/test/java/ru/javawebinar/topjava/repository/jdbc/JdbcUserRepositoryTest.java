package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcUserRepositoryTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;


    @Test
    public void save() {
        String test = "jdbcTEST@mail.com";
        User sampleUser = UserTestData.user;
        sampleUser.setEmail(test);

        User user = repository.get(UserTestData.USER_ID);
        user.setEmail(test);
        service.update(user);

        User userAfterUpdate = service.get(UserTestData.USER_ID);

        assertMatch(userAfterUpdate, sampleUser);
    }

    @Test
    public void delete() {
        repository.delete(UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(UserTestData.USER_ID));
    }

    @Test
    public void get() {
        assertNotNull(service.get(UserTestData.USER_ID));
        assertNotNull(repository.get(UserTestData.USER_ID));
    }

    @Test
    public void getByEmail() {
        assertNotNull(service.getByEmail("userTest@yandex.ru"));
        assertNotNull(repository.getByEmail("userTest@yandex.ru"));
    }

    @Test
    public void getAll() {
        assertNotNull(repository.getAll());
        assertNotNull(service.getAll());
    }
}