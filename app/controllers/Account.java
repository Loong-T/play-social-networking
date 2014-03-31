package controllers;

import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.profile;

import java.util.HashMap;

/**
 * Created by Zheng Xuqiang on 2014/3/26 0026.
 * 用户相关
 */
public class Account extends Controller {
    // TODO
    public static Result user(String uid) {
        User user = User.finder.where().idEq(uid).findUnique();
        HashMap<String, Object> args = new HashMap<>();
        args.put("loginUser", Account.getLoginUser());
        args.put("user", user);
        return ok(profile.render(user.userName + "的资料详情", args));
    }

    public static User getUserById(Long id) {
        return User.finder.where().idEq(id).findUnique();
    }

    /**
     * @return 如果已经登录，返回登录的用户；否则返回null
     */
    public static User getLoginUser() {
        String uid = session("uid");
        String pwd = session("pwd");
        String uname = session("uname");
        if (uid != null && pwd != null && uname != null) {
            return User.finder.where()
                    .eq("userId", uid)
                    .eq("password", pwd)
                    .eq("userName", uname)
                    .eq("activated", true)
                    .findUnique();
        }
        return null;
    }

    public static Result forgetPwd() {
        return play.mvc.Results.TODO;
    }
}
