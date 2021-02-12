package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    MealRestController controller;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (request.getParameter("FilterButton") != null) {
            LocalDate dateStart = LocalDate.parse(request.getParameter("dateStart"));
            LocalDate dateEnd = LocalDate.parse(request.getParameter("dateEnd"));
            LocalTime timeStart = LocalTime.parse(request.getParameter("timeStart"));
            LocalTime timeEnd = LocalTime.parse(request.getParameter("timeEnd"));

            request.setAttribute("meals", controller.getAllMealToByFilterTimeDate(dateStart, dateEnd, timeStart, timeEnd));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);

        } else {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                controller.createMeal(meal);
            } else {
                controller.update(meal);
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
                final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "update":
                int idForUpdate = getId(request);
                request.setAttribute("meal", controller.get(idForUpdate));
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                log.info("filter");
                LocalDate dateStart = LocalDate.parse(getDate(request, "dateStart"));
                LocalDate dateEnd = LocalDate.parse(getDate(request, "dateEnd"));
                LocalTime timeStart = getTime(request, "timeStart");
                LocalTime timeEnd = getTime(request, "timeEnd");

                request.setAttribute("meal", controller.getAllMealToByFilterTimeDate(dateStart, dateEnd,
                        timeStart, timeEnd));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        controller.getAllMealTo());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalTime getTime(HttpServletRequest request, String parametr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH : mm");
        LocalTime localTime = LocalTime.parse(request.getParameter(parametr), df);
        return localTime;
    }

    private String getDate(HttpServletRequest request, String parametr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String localDate = request.getParameter(parametr);
        return localDate;
    }
}
