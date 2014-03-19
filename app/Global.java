import play.GlobalSettings;
import play.mvc.Http;
import play.mvc.SimpleResult;
import play.libs.F.Promise;
import views.html.error.notFound;

import static play.mvc.Results.notFound;

/**
 * Created by Zheng Xuqiang on 14-3-19.
 * Global settings
 * 全局设置
 */
public class Global extends GlobalSettings {

    @Override
    public Promise<SimpleResult> onHandlerNotFound(Http.RequestHeader request) {
        return Promise.<SimpleResult>pure(notFound(
                notFound.render("页面找不到啦(╯﹏╰)b")
        ));
    }
}
