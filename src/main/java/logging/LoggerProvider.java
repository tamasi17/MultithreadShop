import log4Mats.LogManager;
import log4Mats.Logger;

import java.io.File;

public class LoggerProvider {
    private static final File JSON = new File("config\\logConfig.json");
    private static final Logger LOGGER = LogManager.getLoggerFromJson(JSON);

    // Private constructor to prevent instantiation
    private LoggerProvider() {}

    // Accessor
    public static Logger getLogger() {
        return LOGGER;
    }
}