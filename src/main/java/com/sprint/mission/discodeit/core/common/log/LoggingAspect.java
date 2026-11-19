package com.sprint.mission.discodeit.core.common.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(com.sprint.mission.discodeit.core.common.log.MutationLog)")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> argMap = getArgMap(parameterNames, args);

        appendStartLog(className, methodName, argMap);

        Object result = joinPoint.proceed();
        appendEndLog(className, methodName, result);

        return result;
    }

    private Map<String, Object> getArgMap(String[] parameterNames, Object[] args) {
        Map<String, Object> argMap = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            argMap.put(parameterNames[i], args[i]);
        }

        return argMap;
    }

    private void appendStartLog(String className, String methodName, Map<String, Object> argMap) {
        log.debug(
                "[START] {}.{}({})",
                className,
                methodName,
                argMap
        );
    }

    private void appendEndLog(String className, String methodName, Object result) {
        log.info("[END] {}.{}: {}",
                className,
                methodName,
                result);
    }
}
