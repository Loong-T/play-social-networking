package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.HashMap;

public class Application extends Controller {

    public static Result index() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("loginUser", Account.getLoginUser());
        return ok(index.render("主页", args));
    }

}
