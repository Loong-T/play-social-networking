package controllers;

import models.account.User;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Constant;
import utils.Crypt;
import utils.DateUitls;

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
            return badRequest("用户还未登录。");
        }

        models.account.Post post = new models.account.Post();
        post.author = self;
        Boolean validPost = false;
        Http.MultipartFormData body = request().body().asMultipartFormData();

        // 处理文字内容
        String content = body.asFormUrlEncoded().get("content")[0];
        if (content.length() > 140) {
            return badRequest("发布的内容过长。");
        }
        post.content = content;

        // 处理图片
        Http.MultipartFormData.FilePart pic = body.getFile("pic");

        // 当有图片时
        if (pic != null) {
            // 检查MIME TYPE
            String contentType = pic.getContentType();
            if (!contentType.startsWith("image")) {
                return badRequest("上传的不是图片。");
            }
            // 获得文件后缀名
            String fileName = pic.getFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

            File userDir = new File(Constant.UPLOAD_FILE_PATH + self.userId.toString()
                    + "/" + Constant.USER_IMAGES_PATH);

            try {

                FileUtils.forceMkdir(userDir);

                File picFile = pic.getFile();
                // 获得文件的HASH值作为文件名
                // TODO Hash时间过长
                String fileHash = Crypt.digestFile(picFile);

                File picLocal = new File(userDir.getPath() + "/" + fileHash + fileExtension);

                // 如果上传的文件不存在
                if (!picLocal.exists()) {
                    byte[] picBytes = IOUtils.toByteArray(FileUtils.openInputStream(picFile));
                    FileUtils.writeByteArrayToFile(picLocal, picBytes);
                }

                post.pic = picLocal.getName();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } // end if (pic != null)

        // 当Post的内容和图片两者其中一个非空的时候，才进行保存
        validPost = !"".equals(content.trim()) || pic != null;

        if (validPost) {
            post.postTime = DateUitls.now();
            post.save();
        }
        else {
            return badRequest("文字和图片不能都为空。");
        }

        return ok("OK");
    }
}
