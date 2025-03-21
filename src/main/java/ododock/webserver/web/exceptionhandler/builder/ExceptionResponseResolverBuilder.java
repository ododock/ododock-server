package ododock.webserver.web.exceptionhandler.builder;

import ododock.webserver.web.exceptionhandler.response.CompositeExceptionResponseResolver;
import ododock.webserver.web.exceptionhandler.response.ExceptionResponseResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExceptionResponseResolverBuilder {

    private final List<ExceptionResolverFactory> factories;

    public ExceptionResponseResolverBuilder() {
        this.factories = new ArrayList<>();
    }

    public ExceptionResponseResolverBuilder addAllFactories(Collection<ExceptionResolverFactory> factories) {
        this.factories.addAll(factories);
        return this;
    }

    public CompositeExceptionResponseResolver build() {
        List<ExceptionResponseResolver> resolvers = this.factories.stream()
                .map(ExceptionResolverFactory::createExceptionResponseResolver)
                .toList();
        return new CompositeExceptionResponseResolver(resolvers);
    }

}
