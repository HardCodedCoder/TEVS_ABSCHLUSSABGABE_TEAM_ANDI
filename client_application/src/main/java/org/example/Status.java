package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Status {
    private String username;
    private String statusText;
    private LocalDateTime timestamp;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Status(String username, String statusText) {
        this.username = username;
        this.statusText = statusText;
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(formatter);
    }
}
