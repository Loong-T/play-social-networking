package controllers;

import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.showData;

import java.util.HashMap;

public class Application extends Controller {

    public static Result index() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("loginUser", Account.getLoginUser());
        return ok(index.render("主页", args));
    }

    public static Result showUser() {
        return ok(showData.render(User.finder.all()));
    }

}
