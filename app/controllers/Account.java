package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import models.account.Gender;
import models.account.Post;
import models.account.Relationship;
import models.account.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.personalPage;
import views.html.account.profile;
import views.html.account.profileEdit;
import views.html.account.userPosts;
import views.html.message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zheng Xuqiang on 2014/3/26 0026.
 * 用户相关
 */
public class Account extends Controller {

    static HashMap<String, Object> args = new HashMap<>();
    static Form<User> profileForm = Form.form(User.class);

    /**
     * 获取指定Id用户的资料页面
     *
     * @param uid 指定用户Id
     */
    public static Result profile(String uid) {
        Long id = Long.parseLong(uid);
        User user = User.getUserById(id);
        User self = Account.getLoginUser();
        args.clear();

        if (user == null) {
            return badRequest(message.render("不存在的用户", "该用户不存在", args));
        }

        args.put("user", user);
        args.put("self", self);

        // 当用户没有登录时，设置为false
        args.put("followed", self != null && (Relationship.getRelationshipByUser(self.userId, user.userId) != null));

        return ok(profile.render(user.userName + "的资料详情", args));
    }

    /**
     * 获取指定Id用户的主页面
     *
     * @param uid 指定用户Id
     */
    public static Result user(String uid) {
        Long id = Long.parseLong(uid);
        User user = User.getUserById(id);
        User self = Account.getLoginUser();
        args.clear();

        if (user == null) {
            return badRequest(message.render("不存在的用户", "该用户不存在", args));
        }

        args.put("user", user);
        args.put("self", self);
        List<Post> posts = Post.finder
                .where()
                    .and(Expr.eq("author", user),
                            Expr.isNull("group"))
                    .orderBy().desc("postTime")
                .findList();
        args.put("posts", posts);

        return ok(userPosts.render(user.userName + "的信息", args));
    }

    /**
     * @return 如果已经登录，返回登录的用户；否则返回null
     */
    public static User getLoginUser() {
        String uid = session("uid");
        String pwd = session("pwd");
        String uname = session("uname");
        if (uid != null && pwd != null && uname != null) {
            return User.finder.where()
                    .eq("userId", uid)
                    .eq("password", pwd)
                    .eq("userName", uname)
                    .eq("activated", true)
                    .findUnique();
        }
        return null;
    }

    // TODO 忘记密码功能
    public static Result forgetPwd() {
        return play.mvc.Results.TODO;
    }

    /**
     * 返回用户编辑信息的页面
     */
    public static Result edit(String uid) {
        Long id = Long.parseLong(uid);
        User self = getLoginUser();
        args.clear();

        if (!id.equals(self.userId))
            return redirect("/login");

        args.put("user", self);
        args.put("self", self);

        return ok(profileEdit.render("资料修改", args, profileForm.fill(self)));
    }


    /**
     * 将提交的用户信息进行保存
     * TODO 对各信息的检查
     */
    public static Result submit() throws ParseException {
        DynamicForm data = Form.form().bindFromRequest();
        User user = getLoginUser();

        if (!data.get("userId").equals(user.userId.toString())) {
            return redirect("/login");
        }

        switch (data.get("gender")) {
            case "male":
                user.gender = Gender.MALE;
                break;
            case "female":
                user.gender = Gender.FEMALE;
                break;
            default:
                user.gender = Gender.OTHER;
                break;
        }

        String website = data.get("website");
        if (!"".equals(website.trim())) {
            user.website = website;
        }

        String description = data.get("description");
        if (description.length() < 50) {
            user.description = description;
        }

        Date birthday;
        String date = data.get("birthday");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (!"".equals(date) && ((birthday = df.parse(date))) != null) {
            user.birthday = birthday;
        }

        user.save();

        return redirect("/profile?uid=" + user.userId);
    }

    /**
     * 个人页面，可浏览关注用户发布的信息
     */
    public static Result personalPage() {
        User self = getLoginUser();
        args.clear();
        args.put("user", self);
        args.put("self", self);

        if (self == null) {
            return redirect("/login");
        }

        /*
        下面的代码应该没有问题，但由于Ebean存在Bug，导致运行结果错误。
        参见Ebean Bug 402
        两年前（2012）的Bug到现在还没修复
        List<Post> posts = Ebean.find(Post.class)
                .where()
                    .or(
                            Expr.eq("author", self),
                            Expr.eq("author.followers.fromUser", self)
                    )
                    .orderBy().desc("postTime")
                .findList();
        */

        List<Post> posts = Ebean.find(Post.class)
                .where()
                .raw("p_author IN (SELECT p.p_author " +
                                "FROM t_posts p JOIN t_users u ON u.u_id = p.p_author " +
                                "LEFT JOIN t_relationship r ON r.rs_touser = u.u_id " +
                                "WHERE " +
                                "    ( p.p_author = ? " +
                                "     OR r.rs_fromuser = ?)) " +
                                "AND p_group IS NULL " +
                                "ORDER BY p_time DESC ",
                    new Object[]{self.userId, self.userId})
                .findList();
        args.put("posts", posts);

        // 更新最后查看Post时间
//        self.postLastCheck = DateUtils.now();
//        self.update();

        return ok(personalPage.render("个人主页", args));
    }

}
