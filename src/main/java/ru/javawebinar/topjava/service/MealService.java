package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
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


    private final InMemoryMealRepository repository;

    public MealService(InMemoryMealRepository repository) {
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

    public void load(int authUserId) {
        repository.load(authUserId);
    }


    public Meal get(int id, int authUserId) {
        return ValidationUtil.checkNotFoundWithId(repository.get(id, authUserId), id);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getFilteredByDate(userId, startDate, endDate);
    }


}