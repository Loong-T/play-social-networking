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
    static User self = Account.getLoginUser();

    public static Result index() {
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
        args.clear();
        args.put("self", self);

        args.put("users", User.finder.all());

        return ok(listInfo.render("用户列表", "用户", args));
    }

    public static Result groupList() {
        args.clear();
        args.put("self", self);

        args.put("groups", Group.finder.all());

        return ok(listInfo.render("群组列表", "群组", args));
    }
}
