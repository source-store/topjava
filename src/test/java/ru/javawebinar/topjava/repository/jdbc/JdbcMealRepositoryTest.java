package ru.javawebinar.topjava.repository.jdbc;

import junit.framework.TestCase;
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
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest extends TestCase {
    private static final Logger log = LoggerFactory.getLogger(JdbcMealRepositoryTest.class);

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void save() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Optional<Meal> meal = repository.getAll(UserTestData.USER_ID).stream().findFirst();
        int id = meal.get().getId();
        meal.get().setDescription("TestSave");
        service.save(meal.get(), UserTestData.USER_ID);
        Meal mealBack = repository.get(id, UserTestData.USER_ID);
        assertEquals(mealBack, meal.get());
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void delete() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Optional<Meal> meal = repository.getAll(UserTestData.USER_ID).stream().findFirst();
        int id = meal.get().getId();
        repository.delete(id, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(id, UserTestData.USER_ID));
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void get() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        Optional<Meal> meal = repository.getAll(UserTestData.USER_ID).stream().findFirst();
        int id = meal.get().getId();
        Meal mealGet = service.get(id, UserTestData.USER_ID);
        assertNotNull(mealGet);
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getAll() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        List<Meal> meals = repository.getAll(UserTestData.USER_ID);
        assertNotNull(meals);
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }

    @Test
    public void getBetweenHalfOpen() {
        log.info("Start "+ this.getClass().getCanonicalName()+" test");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDate startDate = LocalDate.parse("01.01.1990 12:01", formatter);
        LocalDate endDate = LocalDate.parse("01.01.1991 12:01", formatter);
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, UserTestData.USER_ID);
        if (meals.size() != 0) {
            throw new RuntimeException("Test fail!");
        }
        startDate = LocalDate.parse("01.01.2021 12:01", formatter);
        endDate = LocalDate.parse("01.01.2022 12:01", formatter);
        meals = service.getBetweenInclusive(startDate, endDate, UserTestData.USER_ID);
        if (meals.size() == 0){
            throw new RuntimeException("Test fail!");
        }
        log.info("end "+ this.getClass().getCanonicalName()+" test");
    }
}