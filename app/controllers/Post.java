package controllers;

import models.account.User;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Constant;
import utils.Crypt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zheng Xuqiang on 2014/4/7 0007.
 * 发布Post相关
 */
public class Post extends Controller {

    static Map<String, Object> args = new HashMap<>();

    public static Result newPost() {
        User self = Account.getLoginUser();
        if (self == null) {
            return badRequest();
        }

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart pic = body.getFile("pic");

        if (pic == null) {
            return badRequest();
        }

        // 检查MIME TYPE
        String contentType = pic.getContentType();
        if (!contentType.startsWith("image")) {
            return badRequest();
        }
        // 获得文件后缀名
        String fileName = pic.getFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

        File userDir = new File(Constant.UPLOAD_FILE_PATH + self.userId.toString()
                + File.separator + "images" + File.separator);

        try {

            FileUtils.forceMkdir(userDir);

            File picFile = pic.getFile();
            // 获得文件的HASH值作为文件名
            // TODO Hash时间过长
            String fileHash = Crypt.digestFile(picFile);

            File picLocal = new File(userDir.getPath() + File.separator + fileHash + fileExtension);

            // 如果上传的文件不存在
            if (!picLocal.exists()) {
                byte[] picBytes = IOUtils.toByteArray(FileUtils.openInputStream(picFile));
                FileUtils.writeByteArrayToFile(picLocal, picBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ok("OK");
    }
}
