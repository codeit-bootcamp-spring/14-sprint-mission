package com.sprint.mission.discodeit.adapter.in.web.common.pagination;

import com.sprint.mission.discodeit.core.common.pagination.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public final class PageQueryArgumentResolver implements HandlerMethodArgumentResolver {
    private final PageableHandlerMethodArgumentResolver pageableResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PageQuery.class.equals(parameter.getParameterType());
    }

    @Override
    public PageQuery resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        return PageQueryMapper.fromPageable(
                pageableResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory)
        );
    }
}
