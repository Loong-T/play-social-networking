package controllers;

import models.account.Relationship;
import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.profile;
import views.html.message;

import java.util.HashMap;

/**
 * Created by Zheng Xuqiang on 2014/3/26 0026.
 * 用户相关
 */
public class Account extends Controller {
    public static Result user(String uid) {
        Long id = Long.parseLong(uid);
        User user = User.getUserById(id);
        User self = Account.getLoginUser();
        HashMap<String, Object> args = new HashMap<>();

        if (user == null) {
            return badRequest(message.render("不存在的用户", "该用户不存在", args));
        }

        args.put("followed", Relationship.getRelationshipByUser(self.userId, user.userId) != null);
        args.put("loginUser", Account.getLoginUser());
        args.put("user", user);
        return ok(profile.render(user.userName + "的资料详情", args));
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
