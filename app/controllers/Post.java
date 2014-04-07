package controllers;

import play.db.ebean.Model;
import play.mvc.Result;

/**
 * Created by Zheng Xuqiang on 2014/4/7 0007.
 */
public class Post extends Model {
    public static Result newPost() {
        return play.mvc.Results.TODO;
    }
}
