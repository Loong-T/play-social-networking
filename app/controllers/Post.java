package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by Zheng Xuqiang on 2014/4/7 0007.
 */
public class Post extends Controller {
    public static Result newPost() throws InterruptedException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart pic = body.getFile("pic");
        Thread.sleep(5000);
        return ok("OK");
    }
}
