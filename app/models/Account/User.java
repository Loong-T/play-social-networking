package models.account;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Zheng Xuqiang on 14-3-15.
 * 用户Model类
 */
@Entity
@Table(name = "t_users")
public class User extends Model {

    @Id
    @Column(name = "users_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "users_username", nullable = false,unique = true)
    public String userName;

    @Column(name = "users_email", nullable = false, unique = true)
    @Constraints.Email
    @Constraints.Required
    public String email;

    @Column(name = "users_password")
    @Constraints.Required
    public String password;

    @Column(name = "users_gender")
    @Enumerated
    public Gender gender;

    @Column(name = "users_avatar")
    public String avatar;

    @Column(name = "users_website")
    public String website;

    /**
     * 注册时间
     */
    @Column(name = "users_signup", nullable = false)
    public Date signUp;

    /**
     * 上次登录时间
     */
    @Column(name = "users_lastlogin", nullable = false)
    public Date lastLogin;

    /**
     * 上次查看通知时间
     */
    @Column(name = "users_notescheck", nullable = false)
    public Date notesCheck;

    @Column(name = "user_activated", nullable = false)
    public Boolean activated = false;

    public static Finder<Long, User> finder =
            new Finder<>(Long.class, User.class);

}

enum Gender {MALE, FEMALE, OTHER}
