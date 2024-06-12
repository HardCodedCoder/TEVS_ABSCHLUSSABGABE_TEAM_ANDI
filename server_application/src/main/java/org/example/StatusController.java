package org.example;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api")
public class StatusController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StatusListener statusListener;

    @PostMapping("/setStatus")
    public String setStatus(@RequestBody StatusRequest statusRequest) {
        Status status = new Status(statusRequest.getUsername(), statusRequest.getStatustext());
        rabbitTemplate.convertAndSend("statusExchange", "", status); // Use fanout exchange
        return "Status set successfully";
    }

    @GetMapping("/statuses")
    public List<Status> getAllStatuses() {
        return statusListener.getStatuses();
    }

    @GetMapping("/status/{username}")
    public StatusResponse getStatus(@PathVariable String username) {
        return statusListener.getStatuses().stream()
                .filter(s -> s.getUsername().equals(username))
                .findFirst()
                .map(StatusResponse::new)
                .orElse(null);
    }

    @DeleteMapping("/removeStatus")
    public String removeStatus(@RequestParam String username) {
        rabbitTemplate.convertAndSend("statusExchange", "", new Status(username, "off"));
        return "Status removed successfully";
    }

    public static class StatusRequest {
        private String username;
        private String statustext;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStatustext() {
            return statustext;
        }

        public void setStatustext(String statustext) {
            this.statustext = statustext;
        }
    }

    public static class Status implements Serializable {
        private static final long serialVersionUID = 1L;

        private String username;
        private String statustext;
        private LocalDateTime timestamp;

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public Status() {
            // only used for deserialization
        }

        public Status(String username, String statustext) {
            this.username = username;
            this.statustext = statustext;
            this.timestamp = LocalDateTime.now();
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStatustext() {
            return statustext;
        }

        public void setStatustext(String statustext) {
            this.statustext = statustext;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getFormattedTimestamp() {
            return timestamp.format(formatter);
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    @Getter
    public static class StatusResponse {
        private String username;
        private String statustext;
        private LocalDateTime timestamp;
        private String formattedTimestamp;

        public StatusResponse(Status status) {
            this.username = status.getUsername();
            this.statustext = status.getStatustext();
            this.timestamp = status.getTimestamp();
            this.formattedTimestamp = formatTimestamp(this.timestamp);
        }

        private String formatTimestamp(LocalDateTime timestamp) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return timestamp.format(formatter);
        }
    }
}
