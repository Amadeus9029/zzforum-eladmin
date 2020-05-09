package me.zhengjie.modules.blog.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Amadeus9029
* @date 2020-04-10
*/
@Data
public class BlogDto implements Serializable {

    /** 唯一博文id--主键 */
    private Integer blogId;

    /** 博文标题 */
    private String blogTitle;

    /** 博文内容 */
    private String blogBody;

    /** 博文评论数 */
    private Integer blogDiscusscount;

    /** 博文浏览数 */
    private Integer blogBlogviews;

    /** 博文发布时间 */
    private Timestamp blogTime;

    /** 博文状态--0 删除 1正常 */
    private Integer blogState;

    /** 用户id */
    private Integer userId;
}