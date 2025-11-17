package main.logging;

public enum LogLevel {

    // Log levels, ordered by priority.
    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5);

    private int priority;

    LogLevel(int priority){
        this.priority=priority;
    }

    public int getPriority() {
        return priority;
    }

    // Confirms if the message has to be logged or not, depending on the priority set in the config file.
    public boolean isLoggable(LogLevel configLevel){
        return this.priority <= configLevel.priority;
    }

    public static LogLevel fromString(String level) {
        return LogLevel.valueOf(level.toUpperCase());
    }
}
