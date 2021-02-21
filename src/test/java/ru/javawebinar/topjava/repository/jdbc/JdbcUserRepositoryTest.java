package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.MealServiceTest;
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
    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepositoryTest.class);

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;


    @Test
    public void save() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        String test = "jdbcTEST@mail.com";
        User sampleUser = UserTestData.user;
        sampleUser.setEmail(test);

        User user = repository.get(UserTestData.USER_ID);
        user.setEmail(test);
        service.update(user);

        User userAfterUpdate = service.get(UserTestData.USER_ID);

        assertMatch(userAfterUpdate, sampleUser);
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void delete() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        repository.delete(UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void get() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        assertNotNull(service.get(UserTestData.USER_ID));
        assertNotNull(repository.get(UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getByEmail() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        assertNotNull(service.getByEmail("userTest@yandex.ru"));
        assertNotNull(repository.getByEmail("userTest@yandex.ru"));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getAll() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        assertNotNull(repository.getAll());
        assertNotNull(service.getAll());
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }
}