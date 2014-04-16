package controllers;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import models.account.User;
import org.apache.commons.validator.routines.EmailValidator;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constant;
import utils.Crypt;
import utils.DateUtils;
import utils.ErrorUtils;
import views.html.account.activation;
import views.html.account.signup;
import views.html.error.error;
import views.html.message;

import java.util.HashMap;
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
        session().clear();
        return ok(signup.render("注册新用户", userForm));
    }

    // TODO 已登录的检查；有效性的验证
    public static Result submit() {
        Form<User> form = userForm.bindFromRequest();
        User user = form.get();
        user.postLastCheck = DateUtils.now();
        user.commentLastCheck = DateUtils.now();
        user.save();

        session("uid", user.userId.toString());

        return redirect("/sendemail");
    }

    /**
     * 检查Email是否正确，是否已经注册
     */
    public static Result checkEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        boolean isValidated = validator.isValid(email);

        if (!isValidated) {
            return badRequest("无效的Email地址");
        } else {
            // 检查Email是否已经注册
            int existed = User.finder.where().eq("email", email).findRowCount();

            if (existed > 0) {
                return badRequest("该Email地址已注册");
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
        } else {
            // 检查是否已有相同用户名
            int existed = User.finder.where().eq("userName", username).findRowCount();

            if (existed > 0) {
                return badRequest("该用户名已存在，请更换。");
            }
        }

        return ok("可用的用户名");
    }

    /**
     * 发送激活邮件
     */
    public static Result sendEmail() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("self", Account.getLoginUser());
        User user = User.finder.where().eq("userId", session("uid")).findUnique();
        String actiAddr = Constant.BASE_URL + "/activation?uid="
                + user.userId + "&hash=" + Crypt.bytes2Hex(Crypt.md5(user.salt));

        try {
            MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
            mail.setCharset("UTF-8");
            mail.setSubject("用户账户激活");
            mail.addRecipient(user.userName + "<" + user.email + ">");
            mail.addFrom("Social-Network账户激活<social_networking@126.com>");
            mail.sendHtml("<p>你好" + user.userName
                    + "，</p><p>这封邮件是由Social-Network系统自动发出的账户激活邮件，请点击下面的超链接来激活您的账号（如果链接无法点击，请将地址复制到浏览器地址栏打开）<br><a href=\""
                    + actiAddr + "\">" + actiAddr + "</a></p>如果您没有注册过本网站，请无视这封邮件。请勿回复本邮件。");
        } catch (Exception e) {
            return internalServerError(ErrorUtils.errorPage(
                    "发送激活邮件失败",
                    "出现异常了，我会努力排除错误的\n(╯﹏╰)b",
                    "发送激活邮件失败",
                    500,
                    args));
        }

        return ok(
                message.render("还差一点点就完成了",
                        "现在后台系统已经给您的注册邮箱发出了一封激活邮件，请登录您的邮箱查看并激活您的账户。", null)
        );
    }

    public static Result activation(String uid, String hash) {
        User user = User.finder.where().eq("userId", uid).findUnique();
        if (user != null) {
            if (user.activated) {
                return badRequest(activation.render("账户激活失败", false, "该账户已经激活"));
            }
            else if (hash.equals(Crypt.bytes2Hex(Crypt.md5(user.salt)))) {
                user.activated = true;
                user.save();
                return ok(activation.render("账户激活成功", true, null));
            }
        }
        return badRequest(activation.render("账户激活失败", false, "激活地址错误"));
    }
}
