package ru.javawebinar.topjava.service;
/*
 * @autor A.A.Yakubov
 * */

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.UserTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Optional<Meal> meal = service.getAll(UserTestData.USER_ID).stream().findFirst();
        int idMeal = meal.get().getId();
        assertNotNull(service.get(idMeal, UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void delete() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Optional<Meal> meal = service.getAll(UserTestData.USER_ID).stream().findFirst();
        int idMeal = meal.get().getId();
        service.delete(idMeal, UserTestData.USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> service.get(idMeal, UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getBetweenInclusive() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDate startDate = LocalDate.parse("01.01.1990 12:01", formatter);
        LocalDate endDate = LocalDate.parse("01.01.1991 12:01", formatter);

        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, UserTestData.USER_ID);
        if (meals.size() != 0)
            throw new RuntimeException("Test fail!");

        startDate = LocalDate.parse("01.01.2015 12:01", formatter);
        endDate = LocalDate.parse("01.01.2022 12:01", formatter);

        LocalDate finalStartDate1 = startDate;
        LocalDate finalEndDate1 = endDate;
        assertNotNull(service.getBetweenInclusive(finalStartDate1, finalEndDate1, UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getAll() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        assertNotNull(service.getAll(UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void update() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        String test = "testDescription";
        Optional<Meal> meal = service.getAll(UserTestData.USER_ID).stream().findFirst();
        int idMeal = meal.get().getId();
        meal.get().setDescription(test);
        service.update(meal.get(), UserTestData.USER_ID);
        assertMatch(service.get(idMeal, UserTestData.USER_ID), meal.get());
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void create() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Meal meal = service.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000), UserTestData.USER_ID);
        assertNotNull(service.get(meal.getId(), UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void save() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        String test = "testDescription";
        Optional<Meal> meal = service.getAll(UserTestData.USER_ID).stream().findFirst();
        int idMeal = meal.get().getId();
        meal.get().setDescription(test);

        service.save(meal.get(), UserTestData.USER_ID);
        assertMatch(service.get(idMeal, UserTestData.USER_ID), meal.get());
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }
}