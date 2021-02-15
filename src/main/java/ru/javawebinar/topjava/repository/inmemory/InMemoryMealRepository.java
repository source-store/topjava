package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal meal : MealsUtil.meals) {
            save(meal, SecurityUtil.authUserId());
        }
    }

    @Override
    public void load(int userId) {
        if (repository.get(userId) == null) {
            for (Meal meal : MealsUtil.meals) {
                meal.setId(counter.incrementAndGet());
                repository.putIfAbsent(userId, new HashMap<>());
                repository.get(userId).put(meal.getId(), meal);
            }
        }
    }

    //не понял как убрать этот колхоз, чтобы при выборе пользователя подгружать, при необходимости список еды для него. поэтому загружаю сразу
    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.putIfAbsent(0, new HashMap<>());
            repository.putIfAbsent(1, new HashMap<>());
            repository.get(0).put(meal.getId(), meal);
            repository.get(1).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        Meal old = repository.get(userId) != null
                ? repository.get(userId).get(meal.getId())
                : null;

        return old != null
                ? repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.get(userId) != null && repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(userId) != null ? repository.get(userId).get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.get(userId) != null ? repository.get(userId).values().stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList()) : null;
    }

    @Override
    public synchronized List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenDateTime(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

