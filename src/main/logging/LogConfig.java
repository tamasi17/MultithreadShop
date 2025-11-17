package main.logging;

/**
 * Class that defines a structure for a Json to be Data Bound using Jackson.
 * Handles the logger configuration, receiving the info from a Json file.
 */

public class LogConfig {

    private String status;
    private String filePath;
    private String fileName;
    private long maxFileSize;
    private String pattern;

    /**
     *  Jackson needs an empty constructor
      */
    public LogConfig(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
