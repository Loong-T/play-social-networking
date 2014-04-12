import controllers.Account;
import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http;
import play.mvc.SimpleResult;
import utils.ErrorUtils;
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

        return Promise.<SimpleResult>pure(notFound(
            ErrorUtils.errorPage("错误", "页面找不到啦(╯﹏╰)b", "您所请求的页面不存在", 404, args)
        ));
    }
}
