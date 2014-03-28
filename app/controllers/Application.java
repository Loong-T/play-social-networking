package controllers;

import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.showData;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("项目主页", Login.checkLogin()));
    }

    public static Result showUser() {
        return ok(showData.render(User.finder.all()));
    }
    
}
