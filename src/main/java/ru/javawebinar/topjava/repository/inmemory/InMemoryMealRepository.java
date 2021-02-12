package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
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
            save(meal, 1);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.putIfAbsent(userId, new HashMap<>());
            repository.get(userId).put(meal.getId(), meal);
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
        return repository.get(userId).values().stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }
    @Override
    public List<Meal> getFilter(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, int userId) {
        return repository.values().stream()
                .map(s -> s.get(userId))
                .filter(meal -> meal.getDate().isAfter(dateStart) && meal.getDate().isBefore(dateStart) &&
                        meal.getTime().isAfter(timeStart) && meal.getTime().isBefore(timeEnd))
                .collect(Collectors.toList());
    }


}

