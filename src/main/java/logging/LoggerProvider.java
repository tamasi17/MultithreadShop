package main.java.logging;

import java.io.File;

public class LoggerProvider {
    private static final File JSON = new File("src/main/resources/logConfig.json");
    private static final Logger LOGGER = LogManager.getLoggerFromJson(JSON);

    // Constructor privado para impedir que se instancie
    private LoggerProvider() {}

    // Accedemos al logger con este metodo static
    public static Logger getLogger() {
        return LOGGER;
    }

}