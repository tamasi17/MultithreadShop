import log4Mats.LogLevel;
import log4Mats.Logger;
import logging.LoggerProvider;

public class Main {

    static void main() {
        Logger logger = LoggerProvider.getLogger();
        logger.setLogToConsole(true);

        logger.log(LogLevel.TRACE, ">> Starting shop");


    }
}
