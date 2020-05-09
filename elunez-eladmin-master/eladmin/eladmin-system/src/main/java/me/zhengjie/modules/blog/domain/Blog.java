package me.zhengjie.modules.blog.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Amadeus9029
* @date 2020-04-10
*/
@Entity
@Data
@Table(name="blog")
public class Blog implements Serializable {

    /** 唯一博文id--主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Integer blogId;

    /** 博文标题 */
    @Column(name = "blog_title",nullable = false)
    @NotBlank
    private String blogTitle;

    /** 博文内容 */
    @Column(name = "blog_body",nullable = false)
    @NotBlank
    private String blogBody;

    /** 博文评论数 */
    @Column(name = "blog_discussCount",nullable = false)
    @NotNull
    private Integer blogDiscusscount;

    /** 博文浏览数 */
    @Column(name = "blog_blogViews",nullable = false)
    @NotNull
    private Integer blogBlogviews;

    /** 博文发布时间 */
    @Column(name = "blog_time",nullable = false)
    @NotNull
    private Timestamp blogTime;

    /** 博文状态--0 删除 1正常 */
    @Column(name = "blog_state",nullable = false)
    @NotNull
    private Integer blogState;

    /** 用户id */
    @Column(name = "user_id",nullable = false)
    @NotNull
    private Integer userId;

    public void copy(Blog source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}