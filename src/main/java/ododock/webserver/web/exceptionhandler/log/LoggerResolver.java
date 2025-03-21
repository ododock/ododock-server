package ododock.webserver.web.exceptionhandler.log;

import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

public class LoggerResolver {

    private static Logger createLogger(org.slf4j.Logger logger, LogLevel logLevel) {
        return switch (logLevel) {
            case ERROR -> logger::error;
            case WARN -> logger::warn;
            case INFO -> logger::info;
            case DEBUG -> logger::debug;
            case TRACE -> logger::trace;
            default -> msg -> {
            };
        };
    }

    @NonNull
    private final Map<LogLevel, Logger> loggers;

    public LoggerResolver(Class<?> topicClass) {
        org.slf4j.Logger baseLogger = LoggerFactory.getLogger(topicClass);
        this.loggers = new HashMap<>();
        for (LogLevel level : LogLevel.values()) {
            this.loggers.put(level, createLogger(baseLogger, level));
        }
    }

    public Logger resolve(LogLevel logLevel) {
        return this.loggers.get(logLevel);
    }

}
