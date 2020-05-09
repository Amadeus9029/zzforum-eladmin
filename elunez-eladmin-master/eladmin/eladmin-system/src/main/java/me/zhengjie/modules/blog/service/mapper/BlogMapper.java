package me.zhengjie.modules.blog.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.blog.domain.Blog;
import me.zhengjie.modules.blog.service.dto.BlogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Amadeus9029
* @date 2020-04-10
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BlogMapper extends BaseMapper<BlogDto, Blog> {

}