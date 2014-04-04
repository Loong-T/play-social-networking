package controllers;

import models.account.Relationship;
import models.account.User;
import play.mvc.Controller;
import play.mvc.Result;
import utils.DateUitls;
import views.html.account.followList;
import views.html.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zheng Xuqiang on 2014/4/1 0001.
 * Follow 关注
 */
public class Follow extends Controller {

    /**
     * 为当前登录用户添加指定用户为关注用户
     * @param uid 被关注的用户Id
     */
    public static Result addFollow(String uid) {
        Long userId = Long.parseLong(uid);
        User toUser = User.getUserById(userId);
        User self = Account.getLoginUser();

        // 检查是否登录，且关注用户是否为自己
        if (self != null && !self.userId.equals(userId)) {
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

        if (self != null) {
            Relationship rs = Relationship.finder.where()
                    .eq("fromUser", self)
                    .eq("toUser", User.getUserById(userId))
                    .findUnique();
            if (rs != null) {
                rs.delete();
                return ok();
            }
        }

        return badRequest();
    }

    public static Result list(String uid) {
        Long id = Long.parseLong(uid);
        User user = User.getUserById(id);
        User self = Account.getLoginUser();
        HashMap<String, Object> args = new HashMap<>();

        if (user == null) {
            return badRequest(message.render("不存在的用户", "该用户不存在", args));
        }

        args.put("user", user);
        args.put("loginUser", self);
        if (self != null) {
            args.put("self", user.userId.equals(self.userId));
        }
        else {
            args.put("self", false);
        }

        List<User> users = getFollowedUsers(user);
        args.put("followedUsers", users);

        return ok(followList.render(user.userName + "关注列表", args));
    }

    /**
     * 获取指定用户的关注用户<br>
     * TODO 由于水平有限，用了比较绕圈的方法从数据库中获取数据，这里在数据库中查询了两次，需改进
     * @param self 指定用户
     * @return 该用户关注的用户
     */
    public static List<User> getFollowedUsers(User self) {
        List<User> users = new ArrayList<>();
        for (Relationship rs : Relationship.finder
                                .where()
                                .eq("fromUser", self)
                                .findList()) {
            users.add(User.finder.byId(rs.toUser.userId));
        }

        return users;
    }
}
