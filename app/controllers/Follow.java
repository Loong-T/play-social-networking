package controllers;

import models.account.Relationship;
import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import utils.DateUitls;
import views.html.account.followList;
import views.html.message;

import java.util.HashMap;

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

    // TODO 使用Ebean从两张表中取出关注的人
    public static Result list(String uid) {
        Long id = Long.parseLong(uid);
        User user = User.getUserById(id);
        User self = Account.getLoginUser();
        HashMap<String, Object> args = new HashMap<>();

        if (user == null) {
            return badRequest(message.render("不存在的用户", "该用户不存在", args));
        }

        args.put("loginUser", self);
        args.put("user", user);
        return ok(followList.render("关注列表", args));
    }
}
