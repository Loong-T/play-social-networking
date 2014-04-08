package utils;

import java.io.File;

/**
 * Created by Zheng Xuqiang on 2014/3/25 0025.
 * 一些常量
 */
public class Constant {
    /**
     * 网站的地址
     */
    public static final String BASE_URL = "http://localhost:9000";

    /**
     * 默认时间格式
     */
    public static final String DATE_FORMAT = "yyyy年M月d日 E kk:mm:ss";

    /**
     * 生日格式
     */
    public static final String BIRTHDAY_FORMAT = "yyyy-M-d";

    /**
     * 上传文件存储路径
     */
    public static final String UPLOAD_FILE_PATH = "files" + File.separator + "users" + File.separator;

}
