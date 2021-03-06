package models.account;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Zheng Xuqiang on 2014/4/6 0006.
 * Post的评论
 */
@Entity
@Table(name = "t_comments")
public class Comment extends Model {

    @Id
    @Column(name = "c_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long commentId;

    @JoinColumn(name = "c_post", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    public Post post;

    @JoinColumn(name = "c_author", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    public User author;

    @Column(name = "c_content", length = 512)
    public String content;

    @Column(name = "c_time")
    public Date time;

    public static Finder<Long, Comment> finder = new Finder<>(Long.class, Comment.class);

    @Override
    public boolean equals(Object o) {
        return o instanceof Comment && ((Comment) o).commentId.equals(this.commentId);
    }
}
