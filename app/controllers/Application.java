package controllers;

import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

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
}
