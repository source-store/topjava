package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal createMeal(Meal meal, int authUserId) {
        return repository.save(meal, authUserId);
    }

    public Meal update(Meal meal, int authUserId) {
        return ValidationUtil.checkNotFoundWithId(repository.save(meal, authUserId), meal.getId());
    }

    public void delete(int id, int authUserId) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id, authUserId), id);
    }

    public Meal get(int id, int authUserId) {
        return ValidationUtil.checkNotFoundWithId(repository.get(id, authUserId), id);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<MealTo> getAllMealToByFilterTimeDate(LocalDate dateStart, LocalDate dateEnd,
                                                     LocalTime timeStart, LocalTime timeEnd, int userId) {
        return MealsUtil.getTos(repository.getFilter(dateStart, dateEnd, timeStart,timeEnd,userId), MealsUtil.DEFAULT_CALORIES_PER_DAY).stream()
                .sorted(Comparator.comparing(MealTo::getDateTime).reversed())
                .collect(Collectors.toList());
    }

}