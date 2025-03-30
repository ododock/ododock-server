package ododock.webserver.web.exceptionhandler.response;

import ododock.webserver.util.OrderedComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeExceptionResponseResolver implements ExceptionResponseResolver {

    private final OrderedComparator orderedComparator;
    private final DefaultExceptionResponseResolver defaultExceptionResponseResolver;
    private final Map<Class<? extends Exception>, List<ExceptionResponseResolver>> resolverCache; // Exception에 해당하는 resolver들 캐시에 저장해둠
    private final List<ExceptionResponseResolver> resolvers;

    public CompositeExceptionResponseResolver(List<ExceptionResponseResolver> resolvers) {
        this.orderedComparator = new OrderedComparator();
        this.defaultExceptionResponseResolver = new DefaultExceptionResponseResolver();
        this.resolverCache = new ConcurrentHashMap<>(256);
        this.resolvers = resolvers;
    }

    @Override
    public ExceptionResponse resolve(Exception exception) {
        List<ExceptionResponseResolver> result = this.resolverCache.get(exception.getClass());
        if (result.size() > 0) {
            // resolver가 존재하면 해당 하는 Resolver를 통해서 ExceptionResponse를 생성함
            return result.get(0).resolve(exception);
        }
        // 등록된 Resolver가 없으면 INTERNAL SERVER ERROR로 응답
        return this.defaultExceptionResponseResolver.resolve(exception);
    }

    @Override
    public boolean supports(Class<? extends Exception> exceptionClass) {
        this.resolverCache.computeIfAbsent(exceptionClass, this::createCache);
        return true;
    }

    @Override
    public int getOrder() {
        return ExceptionResponseResolver.super.getOrder();
    }

    private List<ExceptionResponseResolver> createCache(Class<? extends Exception> exceptionClass) {
        List<ExceptionResponseResolver> cache = new ArrayList<>();
        for (ExceptionResponseResolver resolver : this.resolvers) {
            if (resolver.supports(exceptionClass)) {
                cache.add(resolver);
            }
        }
        cache.sort(this.orderedComparator);

        return cache;
    }

}
