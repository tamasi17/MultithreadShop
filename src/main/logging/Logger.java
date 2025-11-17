package main.logging;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.StackWalker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que define un logger por nivel de prioridad de errores:
 * Trace, Debug, Info, Warn, Error, Fatal. (de menor a mayor prioridad).
 * Los logs se guardan en la ruta que nos proporciona el XML o JSON de configuracion.
 * Cuando se llega a maxSize (bytes) se rota fichero.
 */


public class Logger {

    // Singleton from refactoring.guru:
    // This field must be declared volatile so that double check lock would work correctly.
    private static volatile Logger instance;

    private static LogLevel configLevel;
    private File log;
    private final long maxSize;
    private static int logCounter = 1;
    private boolean logToConsole = false;

    // StackWalker instance to get class source (requires Java 9+)
    private static final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    // Date/time format needs to be Windows-friendly.
    // Program runs really fast, had to add milliseconds to the log naming.
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SSS");


    /**
     * Logger constructor will be called from getInstance() to respect Singleton.
     * DOM parsing
     *
     * @param config XML document with configuration info.
     */
    private Logger(Document config) {
        Element e = config.getDocumentElement(); // xml root
        this.configLevel = LogLevel.valueOf(e.getAttribute("status"));
        // getElementsByName() devuelve NodeList, item() devuelve Node, cast a Element para poder usarlo.
        e = (Element) config.getElementsByTagName("MaxFileSize").item(0);
        this.maxSize = Long.parseLong(e.getTextContent());
        e = (Element) config.getElementsByTagName("FilePath").item(0);
        String logPath = e.getTextContent();
        e = (Element) config.getElementsByTagName("FileName").item(0);
        logPath += e.getTextContent();
        this.log = new File(logPath);
    }

    /**
     * Logger constructor will be called from getInstance() to respect Singleton.
     * Jackson Data Binding
     *
     * @param config from Json document with configuration info.
     */
    public Logger(LogConfig config) {
        this.configLevel = LogLevel.valueOf(config.getStatus());
        String logPath = config.getFilePath() + config.getFileName();
        this.log = new File(logPath);
        this.maxSize = config.getMaxFileSize();
        // this.pattern = config.getPattern();
    }


    /**
     * Singleton method that returns an instance only if it does not exist previously.
     * It is called from LogManager's getLogger(File xml).
     *
     * @param configXml
     * @return Logger
     */
    public static Logger getInstance(Document configXml) {
        // Double-checked locking (DCL). Exists to prevent race condition between
        // multiple threads that may attempt to get singleton instance at the same time,
        // creating separate instances as a result.

        Logger result = instance;
        if (result != null) {
            return result;
        }
        synchronized (Logger.class) {
            if (instance == null) {
                instance = new Logger(configXml);
            }
            return instance;
        }
    }

    /**
     * Singleton method that returns an instance only if it does not exist previously.
     * It is called from LogManager's getLoggerFromJson(File json).
     *
     * @param configJson Data binded from LogConfig configuration json
     * @return Logger Singleton instance
     */
    public static Logger getInstance(LogConfig configJson) {
        // Double-checked locking (DCL). Exists to prevent race condition between
        // multiple threads that may attempt to get singleton instance at the same time,
        // creating separate instances as a result.

        Logger result = instance;
        if (result != null) {
            return result;
        }
        synchronized (Logger.class) {
            if (instance == null) {
                instance = new Logger(configJson);
            }
            return instance;
        }
    }

    /**
     * Method that writes a log to the file indicated in the client configuration.
     * Can be shown in console if logToConsole is true.
     * Source class comes from StackWalker (java +9)
     *
     * @see #logToConsole
     * @param level that defines the priority of the log
     * @param message describing the reason for logging
     * @return
     */
    public int log(LogLevel level, String message) {

        confirmLogExists();
        rotateLogIfNeeded();
        String source = getCallerClass();
        String time = LocalDateTime.now().format(FORMATTER);

        if (logToConsole) {
            System.out.println("["+ time +"] ["+ level +"] ["+ source +"]: "+ message);
        }

        // new FileWriter(log, true) to write at the end
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(log, true))) {
            bw.write("[" + logCounter + "] [" + time + "] [" + level + "] [" + source + "]: " + message + "\n");
        } catch (IOException ioe) {
            System.err.println("Error in I/O while logging");
        }

        return ++logCounter;
    }

    /**
     * Method that receives the current log and rotates it if it has reached maxSize.
     * Rotated logs are kept with a new name including a time stamp.
     * Future updates: compress logs when they reach a certain number.
     *
     */
    void rotateLogIfNeeded() {

        long size = log.length();
        if (size < maxSize) return;

        String time = LocalDateTime.now().format(FORMATTER);
        String newLogName = log.getName().replace(".log", "_" + time + ".log");

        System.out.println("------------------------\n" +
                "Log has reached maximum size.\n" +
                "Creating a new log: " + newLogName +
                "\n------------------------");

        try {

            // Files asks for a Path
            Path source = log.toPath();
            // move( Path source, Path target, options...)
            // resolveSibling creates a new path in the same directory but with a new name
            Files.move(source, source.resolveSibling(newLogName));

            Files.createFile(source); // new and empty file, returns Path

        } catch (IOException ioe) {
            System.err.println("Log file was not rotated correctly");
        }

        // ---------------   Compressing log? --> check GZIPOutputStream;

    }


    String getCallerClass(){
        return walker.walk(frames -> frames
                .filter(frame -> !frame.getClassName().equals(Logger.class.getName()))
                .findFirst()
                .map(f -> f.getClassName() + "." + f.getMethodName()) // class + method
                .orElse("Unknown"));
    }


    public void trace(String message) {
        System.out.println("\n· Tracing...");
        if (LogLevel.TRACE.isLoggable(configLevel)) {
            int logID = log(LogLevel.TRACE, message);
            System.out.println("Trace logged. ID:" + logID);
        }
    }

    public void debug(String message) {
        System.out.println("\n· Debug logging...");
        if (LogLevel.DEBUG.isLoggable(configLevel)) {
            int logID = log(LogLevel.DEBUG, message);
            System.out.println("Debug logged. ID:" + logID);
        }
    }

    public void info(String message) {
        System.out.println("\n· Logger retrieving info...");
        if (LogLevel.INFO.isLoggable(configLevel)) {
            int logID = log(LogLevel.INFO, message);
            System.out.println("Info logged. ID:" + logID);
        }
    }

    public void warn(String message) {
        System.out.println("\n· Retrieving warnings...");
        if (LogLevel.WARN.isLoggable(configLevel)) {
            int logID = log(LogLevel.WARN, message);
            System.out.println("Warning logged. ID:" + logID);
        }
    }

    public void error(String message) {
        System.out.println("\n· Logging error...");
        if (LogLevel.ERROR.isLoggable(configLevel)) {
            int logID = log(LogLevel.ERROR, message);
            System.out.println("Error logged. ID:" + logID);
        }
    }

    public void fatal(String message) {
        System.out.println("\n· Program is crashing...");
        if (LogLevel.FATAL.isLoggable(configLevel)) {
            int logID = log(LogLevel.FATAL, message);
            System.out.println("Fatality logged. ID:" + logID);
        }
    }

    /**
     * Method that ensures a log exists, used before operating with it.
     */
    private void confirmLogExists() {
        if (!this.log.exists()) {
            try {
                File parent = log.getParentFile();
                // if log has a parent directory but the directory does not exist
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        throw new IOException("Failed to create log directories: " + parent.getAbsolutePath());
                    }
                }
                if (this.log.createNewFile()) {
                    System.out.println("Log file created: " + log.getAbsolutePath());
                } else {
                    System.out.println("Log file exists. Writing on: " + log.getAbsolutePath());
                }
            } catch (IOException ioe) {
                System.err.println("Log file was not created: " + ioe.getLocalizedMessage());
            }
        }
    }

    /**
     * Log level priority getter.
     */
    public void getLogLevel() {
        System.out.println(configLevel);
    }

    /**
     * If true, shows log in the client console.
     * @return
     */
    public boolean isLogToConsole() {
        return logToConsole;
    }

    /**
     * Set to true to show logs in client console.
     * @param logToConsole
     */
    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }
}

