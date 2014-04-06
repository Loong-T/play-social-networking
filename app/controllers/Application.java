package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.HashMap;

public class Application extends Controller {
    static HashMap<String, Object> args = new HashMap<>();

    public static Result index() {
        args.clear();
        args.put("loginUser", Account.getLoginUser());
        return ok(index.render("主页", args));
    }

    public static Result test() {
        return play.mvc.Results.TODO;
    }
}
