package controllers;

import models.account.User;
import org.apache.commons.validator.routines.EmailValidator;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.signup;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zheng Xuqiang on 14-3-15.
 * 注册Controller
 */
public class SignUp extends Controller {

    /**
     * A form wrapping the User class
     */
    final static Form<User> userForm = Form.form(User.class);

    public static Result blank() {
        return ok(signup.render("注册新用户", userForm));
    }

    // TODO 已登录的检查；有效性的验证
    public static Result submit() {
        Form<User> form = userForm.bindFromRequest();
        User user = form.get();
        user.save();

        return redirect("/showuser");
    }

    /**
     * 检查Email是否正确，是否已经注册
     */
    public static Result checkEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        boolean isValidated = validator.isValid(email);

        if (!isValidated) {
            return badRequest("无效的Email地址");
        }
        else {
            // 检查Email是否已经注册
            int existed = User.finder.where().eq("email", email).findList().size();

            if (existed > 0) {
                return badRequest("该Email地址已注册，您可以尝试使用该邮箱进行登录");
            }
        }

        return ok("有效的Email地址");
    }

    /**
     * 检查用户名是否正确，是否已存在
     */
    public static Result checkUserName(String username) {
        // 检查是否包含空白符
        Pattern p = Pattern.compile("\\t");
        Matcher m = p.matcher(username);
        if (m.matches()) {
            return badRequest("不合规范的用户名，是不是在里面使用了空白符？");
        }
        else {
            // 检查是否已有相同用户名
            int existed = User.finder.where().eq("userName", username).findList().size();

            if (existed > 0) {
                return badRequest("该用户名已存在，请更换。");
            }
        }

        return ok("可用的用户名");
    }
}
