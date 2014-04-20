package controllers;

import models.account.Relationship;
import models.account.User;
import models.group.Group;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Zheng Xuqiang on 2014/4/17 0017.
 */
public class WebService extends Controller {

    static String NAMESPACE = "snns";
    static String NAMESPACE_URL = "http://nerd-is.in/social-network";

    public static Result userService(String uid) {
        User user = User.finder.byId(Long.parseLong(uid));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String message = null;

        // SOAP
        try {
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            soapEnvelope.addNamespaceDeclaration(NAMESPACE, NAMESPACE_URL);

            // Body
            SOAPBody soapBody = soapEnvelope.getBody();

            // User
            SOAPElement soapElemUser = soapBody.addChildElement("User", NAMESPACE);
            soapElemUser.addChildElement("Id", NAMESPACE).addTextNode(user.userId.toString());
            soapElemUser.addChildElement("Name", NAMESPACE).addTextNode(user.userName);
            soapElemUser.addChildElement("Email", NAMESPACE).addTextNode(user.email);
            soapElemUser.addChildElement("Gender", NAMESPACE).addTextNode(user.gender.name());
            soapElemUser.addChildElement("Birthday", NAMESPACE).addTextNode(nullToEmpty(user.birthday));
            soapElemUser.addChildElement("Description", NAMESPACE).addTextNode(nullToEmpty(user.description));
            soapElemUser.addChildElement("Address", NAMESPACE).addTextNode(nullToEmpty(user.address));
            soapElemUser.addChildElement("Website", NAMESPACE).addTextNode(nullToEmpty(user.website));
            soapElemUser.addChildElement("SignUpTime", NAMESPACE).addTextNode(user.signUp.toString());
            soapElemUser.addChildElement("LastLoginTime", NAMESPACE).addTextNode(nullToEmpty(user.lastLogin));

            // FollowingUsers
            SOAPElement soapElemFollowing = soapBody.addChildElement("FollowingUsers", NAMESPACE);
            for (Relationship following : user.followUsers) {
                User followingUser = following.toUser;
                SOAPElement elemFollowing = soapElemFollowing.addChildElement("FollowingUser", NAMESPACE);
                elemFollowing.addChildElement("Id", NAMESPACE).addTextNode(followingUser.userId.toString());
                elemFollowing.addChildElement("Name", NAMESPACE).addTextNode(followingUser.userName);
                elemFollowing.addChildElement("Email", NAMESPACE).addTextNode(followingUser.email);
            }

            // Followers
            SOAPElement soapElemFollowers = soapBody.addChildElement("Followers", NAMESPACE);
            for (Relationship following : user.followUsers) {
                User follower = following.toUser;
                SOAPElement elemFollower = soapElemFollowers.addChildElement("Follower", NAMESPACE);
                elemFollower.addChildElement("Id", NAMESPACE).addTextNode(follower.userId.toString());
                elemFollower.addChildElement("Name", NAMESPACE).addTextNode(follower.userName);
                elemFollower.addChildElement("Email", NAMESPACE).addTextNode(follower.email);
            }

            // Group
            SOAPElement soapElemGroups = soapBody.addChildElement("Groups", NAMESPACE);
            List<Group> groups = user.groups;
            if (groups != null && groups.size() > 0) {
                for (Group group : groups) {
                    SOAPElement elemGroup = soapElemGroups.addChildElement("Group", NAMESPACE);
                    elemGroup.addChildElement("Id", NAMESPACE).addTextNode(group.groupId.toString());
                    elemGroup.addChildElement("Name", NAMESPACE).addTextNode(group.name);
                    elemGroup.addChildElement("Description", NAMESPACE).addTextNode(nullToEmpty(group.description));
                    elemGroup.addChildElement("CreatorId", NAMESPACE).addTextNode(group.creator.userId.toString());
                    elemGroup.addChildElement("CreateTime", NAMESPACE).addTextNode(group.createdTime.toString());

                    SOAPElement soapElemMembers = elemGroup.addChildElement("Members", NAMESPACE);
                    for (User member : group.members) {
                        soapElemMembers.addChildElement("MemberId", NAMESPACE).addTextNode(member.userId.toString());
                    }
                }
            }

            soapMessage.saveChanges();
            soapMessage.writeTo(baos);
            message = new String(baos.toByteArray(), "UTF-8");
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response().setContentType("application/xml");
        return ok(message);
    }

    public static Result userListService() {
        List<User> users = User.finder.all();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String message = null;

        // SOAP
        try {
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            soapEnvelope.addNamespaceDeclaration(NAMESPACE, NAMESPACE_URL);

            // Body
            SOAPBody soapBody = soapEnvelope.getBody();

            // Users
            SOAPElement soapElemUsers = soapBody.addChildElement("Users", NAMESPACE);
            for (User user : users) {
                SOAPElement elemUser = soapElemUsers.addChildElement("User", NAMESPACE);
                elemUser.addChildElement("Id", NAMESPACE).addTextNode(user.userId.toString());
                elemUser.addChildElement("Name", NAMESPACE).addTextNode(user.userName);
                elemUser.addChildElement("Email", NAMESPACE).addTextNode(user.email);
                elemUser.addChildElement("SignUpTime", NAMESPACE).addTextNode(user.signUp.toString());
            }

            soapMessage.saveChanges();
            soapMessage.writeTo(baos);
            message = new String(baos.toByteArray(), "UTF-8");
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response().setContentType("application/xml");
        return ok(message);
    }

    private static String nullToEmpty(String string) {
        if (string == null)
            return "";
        return string;
    }

    private static String nullToEmpty(Date date) {
        if (date == null)
            return "";
        return date.toString();
    }
}
