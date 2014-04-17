package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.account.Comment;
import models.account.Post;
import models.account.Relationship;
import models.account.User;
import models.group.Group;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Constant;
import utils.Crypt;
import utils.DateUtils;
import views.html.unreadComment;
import views.html.unreadPost;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zheng Xuqiang on 2014/4/7 0007.
 * 发布消息相关
 */
public class Message extends Controller {

    static Map<String, Object> args = new HashMap<>();

    public static Result newPost() {
        User self = Account.getLoginUser();
        if (self == null) {
            return badRequest("用户还未登录。");
        }

        Post post = new Post();
        post.author = self;
        Boolean validPost = false;
        Http.MultipartFormData body = request().body().asMultipartFormData();

        // 处理文字内容
        String content = body.asFormUrlEncoded().get("content")[0];
        if (content.length() > 140) {
            return badRequest("发布的内容过长。");
        }
        post.content = content;

        String[] groups = body.asFormUrlEncoded().get("group");
        if (groups != null) {
            String groupId = groups[0];
            Group group = Group.finder.byId(Long.parseLong(groupId));
            if (group == null) {
                return badRequest("不存在该群组");
            }
            else if (!group.members.contains(self)) {
                return badRequest("你不是该群组的成员");
            }
            post.group = group;
        }

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
            post.postTime = DateUtils.now();
            post.save();
        }
        else {
            return badRequest("文字和图片不能都为空。");
        }

        return ok("OK");
    }

    public static Result newComment() {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);
        if (self == null) {
            return badRequest("你没有登录！");
        }

        DynamicForm data = Form.form().bindFromRequest();
        Long postId = Long.parseLong(data.get("post"));
        String cmtContent = data.get("comment");
        Post commentTo = Post.finder.byId(postId);
        if (commentTo == null) {
            return badRequest("你所评论的信息不存在！");
        }
        if (cmtContent == null || "".equals(cmtContent.trim())) {
            return badRequest("评论内容不能为空");
        }

        Comment comment = new Comment();
        comment.author = self;
        comment.post = commentTo;
        comment.content = cmtContent;
        comment.time = DateUtils.now();

        comment.save();

        ObjectNode result = Json.newObject();
        result.put("author", self.userName);
        result.put("authorUrl", "/user?uid=" + self.userId);
        result.put("post", commentTo.postId.toString());
        result.put("comment", cmtContent);

        response().setContentType("application/json");
        return ok(result);
    }

    public static Result unreadCount() {
        User self = Account.getLoginUser();
        if (self == null) {
            return badRequest();
        }

        int unreadPostCount = Post.finder
                .where()
                    .eq("author.followers.fromUser", self)
                    .gt("postTime", self.postLastCheck)
                .findRowCount();

        int unreadCommentCount = Comment.finder
                .where()
                    .eq("post.author", self)
                    .ne("author", self)
                    .gt("time", self.commentLastCheck)
                .findRowCount();

        int unreadFollowerCount = Relationship.finder
                .where()
                    .eq("toUser", self)
                    .gt("makeDate", self.followerLastCheck)
                .findRowCount();

        ObjectNode result = Json.newObject();
        result.put("unreadPostCount", unreadPostCount);
        result.put("unreadCommentCount", unreadCommentCount);
        result.put("unreadFollowerCount", unreadFollowerCount);

        response().setContentType("application/json");
        return ok(String.valueOf(result));
    }

    public static Result unreadComments() {
        User self = Account.getLoginUser();
        if (self == null) {
            return redirect("/login");
        }
        args.clear();
        args.put("self", self);

        List<Post> unreadComments = Post.finder
                .where()
                    .eq("author", self)
                    .gt("comments.time", self.commentLastCheck)
                    .ne("comments.author", self)
                .findList();
        args.put("unreadComments", unreadComments);

        self.commentLastCheck = DateUtils.now();
        self.update();

        return ok(unreadComment.render("新的评论", args));
    }

    public static Result unreadPosts() {
        User self = Account.getLoginUser();
        if (self == null) {
            return redirect("/login");
        }
        args.clear();
        args.put("self", self);

        List<Post> unreadPosts = Post.finder
                .where()
                .eq("author.followers.fromUser", self)
                .gt("postTime", self.postLastCheck)
                .findList();
        args.put("unreadPosts", unreadPosts);

        self.postLastCheck = DateUtils.now();
        self.update();

        return ok(unreadPost.render("新的Post", args));
    }
}
