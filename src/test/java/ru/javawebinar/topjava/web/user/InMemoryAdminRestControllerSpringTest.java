package ru.javawebinar.topjava.web.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;

@ContextConfiguration("classpath:spring/spring-conf.xml")
@RunWith(SpringRunner.class)
public class InMemoryAdminRestControllerSpringTest {
    private static final Logger log = LoggerFactory.getLogger(InMemoryAdminRestControllerSpringTest.class);

    @Autowired
    private AdminRestController controller;

    @Autowired
    private InMemoryUserRepository repository;

    @Before
    public void setUp() {
        repository.init();
    }

    @Test
    public void delete() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        controller.delete(UserTestData.USER_ID);
        Assert.assertNull(repository.get(UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void deleteNotFound() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getAll() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Assert.assertNotNull(controller.getAll());
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void get() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Assert.assertNotNull(controller.get(UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void create() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        User user = controller.create(UserTestData.getNew());
        Assert.assertNotNull(controller.get(user.getId()));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void update() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        String test = "inmemoryTest@mail.ru";
        User user = controller.get(UserTestData.USER_ID);
        user.setEmail(test);
        repository.save(user);
        Assert.assertNotNull(controller.getByMail(test));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }
}