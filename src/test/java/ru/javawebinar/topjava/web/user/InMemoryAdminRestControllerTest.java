package ru.javawebinar.topjava.web.user;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class InMemoryAdminRestControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InMemoryAdminRestControllerTest.class);

    private static ConfigurableApplicationContext appCtx;
    private static AdminRestController controller;
    private static UserRepository repository;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-conf.xml");
        log.info("\n{}\n", Arrays.toString(appCtx.getBeanDefinitionNames()));
        controller = appCtx.getBean(AdminRestController.class);
//        repository = appCtx.getBean(UserRepository.class);
//        repository = appCtx.getBean(InMemoryUserRepository.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Before
    public void setUp() {
        // re-initialize
/*
        UserRepository repository = appCtx.getBean(UserRepository.class);
        repository.getAll().forEach(u -> repository.delete(u.getId()));
        repository.save(UserTestData.user);
        repository.save(UserTestData.admin);
*/
        controller.getAll().forEach(u ->controller.delete(u.getId()));
        controller.create(UserTestData.getNew());
        controller.create(UserTestData.getNew());
    }

    @Test
    public void delete() {
        Optional<User> user = controller.getAll().stream().findFirst();
        System.out.println(user.get().getId());
        controller.delete(user.get().getId());
//        Assert.assertNull(user.get().getId());
        assertThrows(NotFoundException.class, () -> controller.get(USER_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }
}