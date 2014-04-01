package controllers;

import models.account.Relationship;
import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import utils.DateUitls;

/**
 * Created by Zheng Xuqiang on 2014/4/1 0001.
 * Follow 关注
 */
public class Follow extends Controller {
    public static Result addFollow(String uid) {
        Long userId = Long.parseLong(uid);
        User self = Account.getLoginUser();
        if (Login.checkLogin() && !self.userId.equals(userId)) {
            Relationship rs = new Relationship();
            rs.fromUser = self;
            rs.toUser = Account.getUserById(userId);
            rs.makeDate = DateUitls.now();
            rs.save();

            return ok();
        }

        return badRequest();
    }
}
