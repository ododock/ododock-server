package ododock.webserver.web.exceptionhandler.log;

import ododock.webserver.web.exceptionhandler.StatusTypeEnum;

public class DefaultLogLevelResolver implements LogLevelResolver {

    @Override
    public boolean supports(Class<? extends Exception> exceptionClass) {
        return true;
    }

    @Override
    public LogLevel resolve(Exception exception) {
        return StatusTypeEnum.INTERNAL_SERVER_ERROR.getLogLevel();
    }

}
