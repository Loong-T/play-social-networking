package models.account;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Zheng Xuqiang on 14-3-17.
 * 好友关系Model类
 */
@Entity
@Table(name = "t_relationship")
public class Relationship extends Model {

    @Id
    @Column(name = "rs_id")
    public Long id;

    @Column(name = "rs_fromuser", nullable = false)
    @OneToOne(mappedBy = "users_id")
    public User fromUser;

    @Column(name = "rs_touser", nullable = false)
    @OneToOne(mappedBy = "users_id")
    public User toUser;

    @Column(name = "rs_makedate", nullable = false)
    public Date makeDate;

    @Column(name = "rs_accepted", nullable = false)
    public Boolean accepted = false;

    public static Finder<Long, Relationship> finder
            = new Finder<>(Long.class, Relationship.class);

}
