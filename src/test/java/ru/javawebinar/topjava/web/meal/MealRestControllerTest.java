package ru.javawebinar.topjava.web.meal;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

public class MealRestControllerTest {
    private static final Logger log = LoggerFactory.getLogger(MealRestControllerTest.class);

    private static ConfigurableApplicationContext appCtx;
    private static InMemoryMealRepository repository;


    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-conf.xml");
        log.info("\n{}\n", Arrays.toString(appCtx.getBeanDefinitionNames()));
        repository = appCtx.getBean(InMemoryMealRepository.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Test
    public void get() {
        Optional<Meal> meal = repository.getAll(UserTestData.USER_ID).stream().findFirst();
        int idMeal = meal.get().getId();
        assertNotNull(repository.get(idMeal, UserTestData.USER_ID));
    }

    @Test
    public void delete() {
        Optional<Meal> meal = repository.getAll(UserTestData.USER_ID).stream().findFirst();
        int idMeal = meal.get().getId();
        repository.delete(idMeal, UserTestData.USER_ID);
        assertNull(repository.get(idMeal, UserTestData.USER_ID));
    }

    @Test
    public void getAll() {
        Assert.assertNotNull(repository.getAll(UserTestData.USER_ID));
    }

    @Test
    public void create() {
        Meal meal = repository.save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Тест ланч", 510),
                UserTestData.USER_ID);
        assertNotNull(repository.get(meal.getId(), UserTestData.USER_ID));
    }

    @Test
    public void update() {
        String test = "testDescription";
        Optional<Meal> meal = repository.getAll(UserTestData.USER_ID).stream().findFirst();
        int idMeal = meal.get().getId();
        meal.get().setDescription(test);
        repository.save(meal.get(), UserTestData.USER_ID);
        Meal mealAfterUpdate = repository.get(idMeal, UserTestData.USER_ID);
        Assert.assertEquals(meal.get(), mealAfterUpdate);
    }
}