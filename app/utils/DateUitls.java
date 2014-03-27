package utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Zheng Xuqiang on 2014/3/27 0027.
 * 相关常用功能
 */
public class DateUitls {

    /**
     * @return 返回当前时间
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }
}
