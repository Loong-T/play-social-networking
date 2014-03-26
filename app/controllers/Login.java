package controllers;

import models.account.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constant;
import views.html.account.login;

/**
 * Created by Zheng Xuqiang on 2014/3/25 0025.
 * 登录相关Controller
 */
public class Login extends Controller {

    public static Result blank() {
        return ok(login.render("用户登录"));
    }

    public static Result submit() {
        DynamicForm data = Form.form().bindFromRequest();
        String email = data.get("email");
        String pwd = data.get("password");
        
        User user = User.finder.where().eq("email", email).findUnique();
        if (user == null) {
            return unauthorized("该Email没有注册");
        }

        boolean validatedPwd = user.password.equals(User.encryptWithSalt(pwd, user.salt));
        if (!validatedPwd) {
            return unauthorized("错误的密码");
        }

        return ok(Constant.BASE_URL + "/user?uid=" + user.userId);
    }
}
