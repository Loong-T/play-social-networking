package controllers;

import models.account.Post;
import models.account.User;
import models.group.Group;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.DateUtils;
import utils.ErrorUtils;
import views.html.group.group;
import views.html.group.groupMembers;
import views.html.group.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupFun extends Controller {

    static HashMap<String, Object> args = new HashMap<>();

    public static Result groups() {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        List<Group> createdGroups = Group.finder.where().eq("creator", self).findList();
        args.put("created", createdGroups);
        if (self != null) {
            args.put("joined", self.groups);
        }

        return ok(groups.render("我的群组", args));
    }

    public static Result group(String gid) {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        Group thisGroup = Group.finder.byId(Long.parseLong(gid));
        if (thisGroup == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组查看出错", "该群组不存在", 400, args));
        }
        args.put("group", thisGroup);

        List<Post> posts = Post.finder.where().eq("group", thisGroup).orderBy().desc("postTime").findList();
        args.put("posts", posts);

        return ok(group.render("群组", args));
    }

    public static Result joinGroup(String gid) {
        User self = Account.getLoginUser();
        args.clear();
        if (self == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组加入出错", "你还没有登录", 400, args));
        }

        args.put("self", self);
        Group thisGroup = Group.finder.byId(Long.parseLong(gid));
        if (thisGroup == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组加入出错", "该群组不存在", 400, args));
        }
        else if (thisGroup.members.contains(self)) {
            return badRequest(ErrorUtils.errorPage("错误", "群组加入出错", "你已经是该群组的成员", 400, args));
        }

        thisGroup.members.add(self);
        thisGroup.update();

        return redirect("/group?gid=" + gid);
    }

    public static Result leaveGroup(String gid) {
        User self = Account.getLoginUser();
        args.clear();
        if (self == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组退出出错", "你还没有登录", 400, args));
        }

        args.put("self", self);
        Group thisGroup = Group.finder.byId(Long.parseLong(gid));
        if (thisGroup == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组退出出错", "该群组不存在", 400, args));
        }
        else if (!thisGroup.members.contains(self)) {
            return badRequest(ErrorUtils.errorPage("错误", "群组退出出错", "你不是该群组的成员", 400, args));
        }

        thisGroup.members.remove(self);
        thisGroup.update();

        return redirect("/group?gid=" + gid);
    }

    public static Result newGroup() {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        DynamicForm data = Form.form().bindFromRequest();
        Group group = new Group();

        String name = data.get("group-name");
        String description = data.get("group-description");
        if (name == null || name.trim().equals("")) {
            return badRequest(ErrorUtils.errorPage("错误", "群组创建出错", "群组名不能为空", 400, args));
        }

        group.name = name;
        group.description = description;
        group.creator = self;
        ArrayList<User> members = new ArrayList<>();
        members.add(self);
        group.members = members;
        group.createdTime = DateUtils.now();

        group.save();

        return redirect("/groups");
    }

    public static Result delGroup(String gid) {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        Group thisGroup = Group.finder.byId(Long.parseLong(gid));
        if (thisGroup == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组删除出错", "该群组不存在", 400, args));
        }
        else if (!thisGroup.creator.equals(self)) {
            return badRequest(ErrorUtils.errorPage("错误", "群组删除出错", "你不是该群组的创建者", 400, args));
        }

        for (Post post : thisGroup.posts) {
            post.delete();
        }
        for (User member : thisGroup.members) {
            member.groups.remove(thisGroup);
            member.update();
        }
        thisGroup.delete();

        return redirect("/groups");
    }

    public static Result editGroup() {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        DynamicForm data = Form.form().bindFromRequest();
        Long gid = Long.parseLong(data.get("id"));
        String name = data.get("name");
        String description = data.get("description");
        Group thisGroup = Group.finder.byId(gid);
        Group newGroup = new Group();

        if (thisGroup == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组编辑出错", "该群组不存在", 400, args));
        }
        else if (!thisGroup.creator.equals(self)) {
            return badRequest(ErrorUtils.errorPage("错误", "群组编辑出错", "你不是该群组的创建者", 400, args));
        }

        newGroup.groupId = gid;
        if (name != null && !"".equals(name.trim())) {
            newGroup.name = name;
        }
        if (description != null) {
            newGroup.description = description;
        }
        newGroup.update();

        return redirect("/group?gid=" + gid);
    }

    public static Result members(String gid) {
        User self = Account.getLoginUser();
        args.clear();
        args.put("self", self);

        Group thisGroup = Group.finder.byId(Long.parseLong(gid));
        if (thisGroup == null) {
            return badRequest(ErrorUtils.errorPage("错误", "群组删除出错", "该群组不存在", 400, args));
        }
        args.put("group", thisGroup);
        args.put("members", thisGroup.members);

        return ok(groupMembers.render("成员列表", args));
    }
}