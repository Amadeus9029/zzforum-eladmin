package me.zhengjie.modules.blog.service.impl;

import me.zhengjie.modules.blog.domain.Blog;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.modules.blog.repository.BlogRepository;
import me.zhengjie.modules.blog.service.BlogService;
import me.zhengjie.modules.blog.service.dto.BlogDto;
import me.zhengjie.modules.blog.service.dto.BlogQueryCriteria;
import me.zhengjie.modules.blog.service.mapper.BlogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author Amadeus9029
* @date 2020-04-10
*/
@Service
//@CacheConfig(cacheNames = "blog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    private final BlogMapper blogMapper;

    public BlogServiceImpl(BlogRepository blogRepository, BlogMapper blogMapper) {
        this.blogRepository = blogRepository;
        this.blogMapper = blogMapper;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(BlogQueryCriteria criteria, Pageable pageable){
        Page<Blog> page = blogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(blogMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<BlogDto> queryAll(BlogQueryCriteria criteria){
        return blogMapper.toDto(blogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public BlogDto findById(Integer blogId) {
        Blog blog = blogRepository.findById(blogId).orElseGet(Blog::new);
        ValidationUtil.isNull(blog.getBlogId(),"Blog","blogId",blogId);
        return blogMapper.toDto(blog);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public BlogDto create(Blog resources) {
        return blogMapper.toDto(blogRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Blog resources) {
        Blog blog = blogRepository.findById(resources.getBlogId()).orElseGet(Blog::new);
        ValidationUtil.isNull( blog.getBlogId(),"Blog","id",resources.getBlogId());
        blog.copy(resources);
        blogRepository.save(blog);
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            blogRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<BlogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BlogDto blog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("博文标题", blog.getBlogTitle());
            map.put("博文内容", blog.getBlogBody());
            map.put("博文评论数", blog.getBlogDiscusscount());
            map.put("博文浏览数", blog.getBlogBlogviews());
            map.put("博文发布时间", blog.getBlogTime());
            map.put("博文状态--0 删除 1正常", blog.getBlogState());
            map.put("用户id", blog.getUserId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}