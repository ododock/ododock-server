package ododock.webserver.web.exceptionhandler.response;

import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

public interface ExceptionResponseResolver extends Ordered {

    /**
     * Exception 객체를 ExceptionResponse로 변환함
     * @param exception
     * @return
     */
    ExceptionResponse resolve(Exception exception);

//    default ExceptionResponse resolve(ServerWebExchange exchange, Exception exception) {
//        return resolve(exception);
//    }

    boolean supports(Class<? extends Exception> exceptionClass);

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
