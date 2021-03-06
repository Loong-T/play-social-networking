package controllers;

import models.account.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.DateUtils;
import views.html.account.login;

import java.util.HashMap;

/**
 * Created by Zheng Xuqiang on 2014/3/25 0025.
 * 登录相关Controller
 */
public class Login extends Controller {


    static HashMap<String, Object> args = new HashMap<>();

    public static Result blank() {
        return ok(login.render("用户登录"));
    }

    public static Result submit() {
        args.clear();
        if (checkLogin()) {
            args.put("self", Account.getLoginUser());
            return badRequest("您的账户已经登录，无需重复登录");
        }

        DynamicForm data = Form.form().bindFromRequest();
        String email = data.get("email");
        String pwd = data.get("password");
        String autoLogin = data.get("remember");

        if (email == null || "".equals(email)
                || pwd == null || "".equals(pwd)) {
            return unauthorized("错误的数据");
        }

        User user = User.finder.where().eq("email", email).findUnique();
        if (user == null) {
            return unauthorized("该Email没有注册");
        }

        if (!user.activated) {
            return unauthorized("该账号还未通过邮箱验证激活<br>请查看邮箱进行激活");
        }

        boolean validatedPwd = user.password.equals(User.encryptWithSalt(pwd, user.salt));
        if (!validatedPwd) {
            return unauthorized("错误的密码");
        }

        user.lastLogin = DateUtils.now();
        user.save();

        session("uid", user.userId.toString());
        session("uname", user.userName);
        session("pwd", user.password);

        response().setCookie("email", user.email);
        if ("true".equals(autoLogin)) {
            response().setCookie("remember-me", user.userId + ":" + user.password, 3600 * 24 * 30);
        }
        return ok("/personal-page");
    }

    public static Result logout() {
        session().clear();
        // TODO 检查登出状态，返回相应的页面
        return redirect("/");
    }

    /**
     * @return 用户是否已登录
     */
    public static boolean checkLogin() {
        String uid = session("uid");
        String pwd = session("pwd");
        String uname = session("uname");
        if (uid != null && pwd != null && uname != null) {
            return 0 < User.finder.where()
                    .eq("userId", uid)
                    .eq("password", pwd)
                    .eq("userName", uname)
                    .eq("activated", true)
                    .findRowCount();
        }
        return false;
    }
}
