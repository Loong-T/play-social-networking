package controllers;

import models.account.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.signup;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Zheng Xuqiang on 14-3-15.
 * 注册Controller
 */
public class SignUp extends Controller {

    /**
     * A form wrapping the User class
     */
    final static Form<User> userForm = Form.form(User.class);

    public static Result blank() {
        return ok(signup.render("注册新用户", userForm));
    }

    // TODO
    public static Result submit() {
        Form<User> form = userForm.bindFromRequest();
        User user = form.get();
        user.userName = UUID.randomUUID().toString();
        Date now = Calendar.getInstance().getTime();
        user.signUp = now;
        user.lastLogin = now;
        user.notesCheck = now;
        user.save();

        return redirect("/showuser");
    }
}
