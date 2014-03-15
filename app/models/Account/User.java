package models.account;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;

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

    @Column(name = "users_username")
    public String userName;

    @Column(name = "users_email")
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

    @Column(name = "users_signup")
    public Timestamp signUpDate;

    @Column(name = "users_lastlogin")
    public Timestamp lastLogin;

    @Column(name = "users_notescheck")
    public Timestamp notesCheck;

    @Column(name = "user_activated")
    public Boolean activated = false;

    public static Finder<Long, User> finder =
            new Finder<Long, User>(Long.class, User.class);

}

enum Gender {MALE, FEMALE, OTHER}
