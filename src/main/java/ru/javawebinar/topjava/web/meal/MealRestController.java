package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;


@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }


    public synchronized void createMeal(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        service.createMeal(meal, SecurityUtil.authUserId());
    }

    public synchronized void update(Meal meal) {
        log.info("upate {}", meal);
        service.update(meal, SecurityUtil.authUserId());
    }
    public synchronized void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public synchronized void load (){
        service.load(SecurityUtil.authUserId());
    }

    public synchronized Collection<Meal> getAll() {
        log.info("getAll");
        return service.getAll(SecurityUtil.authUserId());
    }

    public synchronized List<MealTo> getAllMealTo() {
        List<Meal> resultMeals = service.getAll(SecurityUtil.authUserId());
        return resultMeals != null ? MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY) : null;
    }

    public synchronized List<MealTo> getFilteredByDateAndTime(HttpServletRequest request){
        log.info("getAllFiltered");
        String startD = request.getParameter("startDate");
        String endD = request.getParameter("endDate");
        String startT = request.getParameter("startTime");
        String endT = request.getParameter("endTime");

        LocalDate startDate = (startD == null || startD.isEmpty())? LocalDate.MIN : LocalDate.parse(startD);
        LocalDate endDate = (endD == null || endD.isEmpty())? LocalDate.MAX : LocalDate.parse(endD);
        LocalTime startTime = (startT == null || startT.isEmpty())? LocalTime.MIN : LocalTime.parse(startT);
        LocalTime endTime = (endT == null || endT.isEmpty())? LocalTime.MAX.withNano(0).withSecond(0) : LocalTime.parse(endT);

        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("startTime", startTime);
        request.setAttribute("endTime", endTime);

        return MealsUtil.getFilteredByTime(service.getFilteredByDate(SecurityUtil.authUserId(), startDate, endDate),startTime,endTime, SecurityUtil.authUserCaloriesPerDay());
    }

}