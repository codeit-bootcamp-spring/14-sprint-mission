package com.sprint.mission.discodeit.adapter.in.web.common.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class PaginationWebConfig implements WebMvcConfigurer {
    private final PageableHandlerMethodArgumentResolver pageableResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageQueryArgumentResolver(pageableResolver));
    }
}
