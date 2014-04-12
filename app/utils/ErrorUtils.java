package utils;

import play.api.templates.Html;
import views.html.error.error;

import java.util.Map;

/**
 * Created by Zheng Xuqiang on 2014/4/12 0012.
 * 错误页面常用功能
 */
public class ErrorUtils {
    /**
     * 返回错误页面的Html
     * @param title 页面title
     * @param msg 错误消息
     * @param details 详细信息
     * @param status 错误状态码
     * @param pageArgs 页面所需要的参数
     * @return 错误页面的Html
     */
    public static Html errorPage(String title, String msg, String details, int status, Map pageArgs) {
        pageArgs.put("errorMsg", msg);
        pageArgs.put("detailMsg", details);
        pageArgs.put("status", status);
        return error.render(title, pageArgs);
    }
}
