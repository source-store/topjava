package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class ProfileRestController extends AbstractUserController {

    @Autowired
    public ProfileRestController(UserService service) {
        super(service);
    }

    public User get() {
        return super.get(authUserId());
    }

    public void delete() {
        super.delete(authUserId());
    }

    public void update(User user) {
        super.update(user, authUserId());
    }

    @Override
    public void change(int userId) {
        super.change(userId);
    }
}