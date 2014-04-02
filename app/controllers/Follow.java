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
        User toUser = User.getUserById(userId);
        if (Login.checkLogin() && !self.userId.equals(userId)) {
            if (Relationship.getRelationshipByUser(self.userId, toUser.userId) == null) {
                Relationship rs = new Relationship();
                rs.fromUser = self;
                rs.toUser = toUser;
                rs.makeDate = DateUitls.now();
                rs.save();
            }

            return ok();
        }

        return badRequest();
    }

    public static Result cancleFollow(String uid) {
        Long userId = Long.parseLong(uid);
        User self = Account.getLoginUser();
        Relationship rs = Relationship.finder.where()
                .eq("fromUser", self)
                .eq("toUser", User.getUserById(userId))
                .findUnique();
        if (rs != null) {
            rs.delete();
            return ok();
        }
        return badRequest();
    }
}
