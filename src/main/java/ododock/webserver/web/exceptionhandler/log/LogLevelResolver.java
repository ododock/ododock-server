package ododock.webserver.web.exceptionhandler.log;

import org.springframework.core.Ordered;

public interface LogLevelResolver extends Ordered {

    LogLevel resolve(Exception exception);

    /**
     * Log Level을 가져올 수 있는 Exception인지 확인
     * @param exceptionClass
     * @return
     */
    boolean supports(Class<? extends Exception> exceptionClass);

    /**
     * 왜 Ordere를 가장 낮도록 하는가?
     * @return
     */
    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }


}
