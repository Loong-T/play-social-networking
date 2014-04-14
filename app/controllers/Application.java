package controllers;

import models.account.User;
import models.group.Group;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.listInfo;

import java.util.HashMap;

public class Application extends Controller {
    static HashMap<String, Object> args = new HashMap<>();

    public static Result index() {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        if (self == null) {
            return ok(index.render("主页", args));
        }
        else {
            return redirect("/personal-page");
        }
    }

    public static Result test() {
        return play.mvc.Results.TODO;
    }

    public static Result userList() {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        args.put("users", User.finder.all());

        return ok(listInfo.render("用户列表", "用户", args));
    }

    public static Result groupList() {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        args.put("groups", Group.finder.all());

        return ok(listInfo.render("群组列表", "群组", args));
    }
}
