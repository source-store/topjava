package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class IndexServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    ProfileRestController profileRestController;
/*
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
//        profileRestController = appCtx.getBean(ProfileRestController.class);
    }
*/
/*
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        SecurityUtil.setAuthUserId(userId);
        response.sendRedirect("index");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "get":
                int userId = Integer.parseInt(request.getParameter("userId"));
                profileRestController.change(userId);
                response.sendRedirect("meals");
                break;
            case "all":
            default:
                log.info("getAll");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
        }


    }
 */

}
