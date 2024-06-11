package org.example;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api")
public class StatusController {

    private final List<Status> statuses = new CopyOnWriteArrayList<>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StatusListener statusListener;

    @PostMapping("/setStatus")
    public String setStatus(@RequestBody StatusRequest statusRequest) {
        Status status = new Status(statusRequest.getUsername(), statusRequest.getStatustext());
        statuses.add(status);
        rabbitTemplate.convertAndSend("statusExchange", "", status); // Use fanout exchange
        return "Status set successfully";
    }

    @GetMapping("/statuses")
    public List<Status> getAllStatuses() {
        return new ArrayList<>(statuses);
    }

    @GetMapping("/status/{username}")
    public StatusResponse getStatus(@PathVariable String username) {
        return statusListener.getStatuses().stream()
                .filter(s -> s.getUsername().equals(username))
                .findFirst()
                .map(StatusResponse::new)
                .orElse(null);
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

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    @Getter
    public static class StatusResponse {
        private String username;
        private String statustext;
        private LocalDateTime timestamp;

        public StatusResponse(Status status) {
            this.username = status.getUsername();
            this.statustext = status.getStatustext();
            this.timestamp = status.getTimestamp();
        }
    }
}
