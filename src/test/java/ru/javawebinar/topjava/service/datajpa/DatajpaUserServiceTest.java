package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DatajpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals (){
        User user = service.getWithMeals(UserTestData.user.getId());
        System.out.println(user.getMeals());
        System.out.println(MealTestData.meals);

        MEAL_MATCHER.assertMatch(MealTestData.meals, user.getMeals());
    }

}
