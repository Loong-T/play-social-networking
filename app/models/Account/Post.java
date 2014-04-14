package models.account;

import models.group.Group;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Zheng Xuqiang on 2014/4/6 0006.
 * 发布的信息
 */
@Entity
@Table(name = "t_posts")
public class Post extends Model {

    @Id
    @Column(name = "p_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long postId;

    @JoinColumn(name = "p_author", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    public User author;

    @Column(name = "p_content", length = 512)
    public String content;

    @Column(name = "p_time")
    public Date postTime;

    @Column(name = "p_pic")
    public String pic;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    public List<Comment> comments;

    @JoinColumn(name = "p_group", updatable = false)
    @ManyToOne(optional = true)
    public Group group;

    public static Finder<Long, Post> finder = new Finder<>(Long.class, Post.class);

    @Override
    public boolean equals(Object o) {
        return o instanceof Post && ((Post) o).postId.equals(this.postId);
    }
}
