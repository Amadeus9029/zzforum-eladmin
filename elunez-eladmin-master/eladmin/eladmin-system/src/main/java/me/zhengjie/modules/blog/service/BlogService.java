package me.zhengjie.modules.blog.service;

import me.zhengjie.modules.blog.domain.Blog;
import me.zhengjie.modules.blog.service.dto.BlogDto;
import me.zhengjie.modules.blog.service.dto.BlogQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Amadeus9029
* @date 2020-04-10
*/
public interface BlogService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(BlogQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BlogDto>
    */
    List<BlogDto> queryAll(BlogQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param blogId ID
     * @return BlogDto
     */
    BlogDto findById(Integer blogId);

    /**
    * 创建
    * @param resources /
    * @return BlogDto
    */
    BlogDto create(Blog resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(Blog resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<BlogDto> all, HttpServletResponse response) throws IOException;
}