package ododock.webserver.web.exceptionhandler.log;

import lombok.NonNull;
import ododock.webserver.util.OrderedComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeLogLevelResolver implements LogLevelResolver {

    @NonNull
    private final OrderedComparator orderedComparator;
    @NonNull
    private final DefaultLogLevelResolver defaultLogLevelResolver;
    @NonNull
    private final Map<Class<? extends Exception>, List<LogLevelResolver>> resolverCache;
    @NonNull
    private final List<LogLevelResolver> resolvers;

    public CompositeLogLevelResolver(List<LogLevelResolver> resolvers) {
        this.orderedComparator = new OrderedComparator();
        this.defaultLogLevelResolver = new DefaultLogLevelResolver();
        this.resolverCache = new ConcurrentHashMap<>(256);
        this.resolvers = resolvers;
    }

    @Override
    public LogLevel resolve(Exception exception) {
        List<LogLevelResolver> result = this.resolverCache.get(exception.getClass());
        if (result.size() > 0) {
            return result.get(0).resolve(exception);
        }
        return this.defaultLogLevelResolver.resolve(exception);
    }

    @Override
    public boolean supports(Class<? extends Exception> exceptionClass) {
        List<LogLevelResolver> result = this.resolverCache.get(exceptionClass);
        if (result == null) {
            result = new ArrayList<>();
            this.resolverCache.put(exceptionClass, result);
            for (LogLevelResolver resolver : this.resolvers) {
                if (resolver.supports(exceptionClass)) {
                    result.add(resolver);
                }
            }
            result.sort(this.orderedComparator);
        }
        return true;
    }

}
