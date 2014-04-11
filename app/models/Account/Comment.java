package models.account;

import play.db.ebean.Model;

import javax.persistence.*;

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

}
