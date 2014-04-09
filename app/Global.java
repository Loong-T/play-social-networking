import controllers.Account;
import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http;
import play.mvc.SimpleResult;
import views.html.error.error;

import java.util.HashMap;

import static play.mvc.Results.notFound;

/**
 * Created by Zheng Xuqiang on 14-3-19.
 * Global settings
 * 全局设置
 */
public class Global extends GlobalSettings {

    @Override
    public Promise<SimpleResult> onHandlerNotFound(Http.RequestHeader request) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("loginUser", Account.getLoginUser());
        args.put("status", 404);
        args.put("errorMsg", "页面找不到啦(╯﹏╰)b");
        args.put("detailMsg", "您所请求的页面不存在");
        return Promise.<SimpleResult>pure(notFound(
                error.render("找不到该页面", args)
        ));
    }
}
