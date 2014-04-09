package models.account;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;

import javax.persistence.*;
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

//    @JoinColumn(name = "rs_fromuser", referencedColumnName = "users_id",
//            nullable = false, updatable=false)
//    @OneToOne(optional = false, targetEntity = User.class)

    @JoinColumn(name = "rs_fromuser", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    public User fromUser;

//    @JoinColumn(name = "rs_touser", referencedColumnName = "users_id",
//            nullable = false, updatable=false)
//    @OneToOne(optional = false, targetEntity = User.class)

    @JoinColumn(name = "rs_touser", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    public User toUser;

    @Column(name = "rs_makedate", nullable = false)
    public Date makeDate;

    public static Finder<Long, Relationship> finder
            = new Finder<>(Long.class, Relationship.class);

    @Transient
    public static Relationship getRelationshipByUser(Long fromUser, Long toUser) {
        return Ebean.find(Relationship.class).where()
                .eq("rs_fromuser", fromUser)
                .eq("rs_touser", toUser)
                .findUnique();
    }

}
