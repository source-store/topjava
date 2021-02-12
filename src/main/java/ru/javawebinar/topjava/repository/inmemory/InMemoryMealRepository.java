package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            save(meal, 2);
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
        LocalDate dateStartFilter = dateStart != null ? dateStart : LocalDate.MIN;
        LocalDate dateEndFilter = dateEnd != null ? dateEnd : LocalDate.MAX;
        dateEndFilter = dateEndFilter.isBefore(dateStartFilter) ? dateStartFilter : dateEndFilter;

        LocalTime timeStartFilter = timeStart != null ? timeStart : LocalTime.MIN;
        LocalTime timeEndFilter = timeEnd != null ? timeEnd : LocalTime.MAX;
        timeEndFilter = timeEndFilter.isBefore(timeStartFilter) ? timeStartFilter : timeEndFilter;

        LocalDateTime finalDateStartFilter = dateStartFilter.atTime(timeStartFilter);
        LocalDateTime finalDateEndFilter = dateEndFilter.atTime(timeEndFilter);

        return repository.get(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenDateTime(meal.getDateTime(), finalDateStartFilter, finalDateEndFilter) )
//                .filter(meal -> meal.getDate().is .isAfter(dateStartFilter) && meal.getDate().isBefore(finalDateEndFilter) && meal.getTime().isAfter(timeStart) && meal.getTime().isBefore(timeEnd))
                .collect(Collectors.toList());
    }


    public synchronized List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenDateTime(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

