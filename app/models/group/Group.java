package models.group;

import models.account.Post;
import models.account.User;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Zheng Xuqiang on 2014/4/11 0011.
 * 群组
 */
@Entity
@Table(name = "t_groups")
public class Group extends Model {

    @Id
    @Column(name = "g_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long groupId;

    @Column(name = "g_name")
    public String name;

    @Column(name = "g_description", length = 512)
    public String description;

    @JoinColumn(name = "g_creator")
    @OneToOne(optional = false)
    public User creator;

    @Column(name = "g_created_time")
    public Date createdTime;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    public List<User> members;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    public List<Post> posts;

    public static Finder<Long, Group> finder = new Finder<>(Long.class, Group.class);

    @Override
    public boolean equals(Object o) {
        return o instanceof Group && ((Group) o).groupId.equals(this.groupId);
    }

}
