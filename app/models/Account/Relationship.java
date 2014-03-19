package models.account;

import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by Zheng Xuqiang on 14-3-17.
 * friends relationship model class
 * 好友关系Model类
 */
@Entity
@Table(name = "t_relationship")
public class Relationship extends Model {

    @Id
    @Column(name = "rs_id")
    public Long relationshipId;

    @JoinColumn(name = "rs_fromuser", referencedColumnName = "users_id",
            nullable = false, updatable=false)
    @OneToOne(optional = false, targetEntity = User.class)
    public User fromUserId;

    @JoinColumn(name = "rs_touser", referencedColumnName = "users_id",
            nullable = false, updatable=false)
    @OneToOne(optional = false, targetEntity = User.class)
    public User toUserId;

    @Column(name = "rs_makedate", nullable = false)
    public Date makeDate;

    @Column(name = "rs_accepted", nullable = false)
    public Boolean accepted = false;

    public static Finder<Long, Relationship> finder
            = new Finder<>(Long.class, Relationship.class);

}
