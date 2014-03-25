package models.account;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.Crypt;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Zheng Xuqiang on 14-3-15.
 * User model class
 * 用户Model类
 */
@Entity
@Table(name = "t_users")
public class User extends Model {

    @Id
    @Column(name = "users_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long userId;

    @Column(name = "users_username", nullable = false,unique = true)
    public String userName;

    @Column(name = "users_email", nullable = false, unique = true)
    @Constraints.Email
    @Constraints.Required
    public String email;

    @Column(name = "users_password")
    @Constraints.Required
    @Constraints.Pattern(value = "^\\w{6,128}$",
            message = "密码的长度在6-128之间，可以使用的字符有数字，字母和下划线")
    public String password;

    @Column(name = "users_salt")
    public String salt;

    @Column(name = "users_gender")
    @Enumerated
    public Gender gender;

    @Column(name = "users_avatar")
    public String avatar;

    @Column(name = "users_website")
    public String website;

    /**
     * Sign up date
     * 注册时间
     */
    @Column(name = "users_signup", nullable = false)
    public Date signUp;

    /**
     * Last login date
     * 上次登录时间
     */
    @Column(name = "users_lastlogin", nullable = false)
    public Date lastLogin;

    /**
     * Last check notification date
     * 上次查看通知时间
     */
    @Column(name = "users_notescheck", nullable = false)
    public Date notesCheck;

    @Column(name = "user_activated", nullable = false)
    public Boolean activated = false;

    public User() {
        Date now = Calendar.getInstance().getTime();
        this.signUp = now;
        this.lastLogin = now;
        this.notesCheck = now;
    }

    /**
     * 进行salt加密
     * @param password 初始未加密的密码
     */
    public void setPassword(String password) {
        this.salt = Crypt.genSalt();
        this.password = encryptWithSalt(password, salt);
    }

    /**
     * 对传入的密码和salt值进行加密
     * @param password 密码
     * @param salt salt值
     * @return 加密后字符串
     */
    private String encryptWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update((password + salt).getBytes());
            return Crypt.bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Finder<Long, User> finder =
            new Finder<>(Long.class, User.class);

}

enum Gender {MALE, FEMALE, OTHER}
