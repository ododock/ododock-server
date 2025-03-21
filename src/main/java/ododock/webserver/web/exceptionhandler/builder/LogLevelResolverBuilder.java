package ododock.webserver.web.exceptionhandler.builder;

import ododock.webserver.web.exceptionhandler.log.CompositeLogLevelResolver;
import ododock.webserver.web.exceptionhandler.log.LogLevelResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogLevelResolverBuilder {

    private final List<ExceptionResolverFactory> factories;

    public LogLevelResolverBuilder() {
        this.factories = new ArrayList<>();
    }

    public LogLevelResolverBuilder addAllFactories(Collection<ExceptionResolverFactory> factories) {
        this.factories.addAll(factories);
        return this;
    }

    public CompositeLogLevelResolver build() {
        List<LogLevelResolver> resolvers = this.factories.stream()
                .map(ExceptionResolverFactory::createLogLevelResolver)
                .toList();
        return new CompositeLogLevelResolver(resolvers);
    }

}
