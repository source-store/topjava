package ru.javawebinar.topjava.service;
/*
 * @autor A.A.Yakubov
 * */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.user.InMemoryAdminRestControllerSpringTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private UserService service;

    @Test
    public void create() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        User created = service.create(getNew());
        Integer newId = created.getId();
        User newUser = getNew();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void duplicateMailCreate() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void delete() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void deletedNotFound() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void get() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        User user = service.get(ADMIN_ID);
        assertMatch(UserTestData.admin, user);
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getNotFound() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getByEmail() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        User user = service.getByEmail("adminTest@gmail.com");
        assertMatch(user, admin);
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void update() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        User updated = getUpdated();
        service.update(updated);
        assertMatch(service.get(USER_ID), getUpdated());
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getAll() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        List<User> all = service.getAll().stream().filter(usr -> usr.getId() == USER_ID || usr.getId() == ADMIN_ID).collect(Collectors.toList());
        System.out.println(all.toString());
        assertNotNull(all);
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }
}