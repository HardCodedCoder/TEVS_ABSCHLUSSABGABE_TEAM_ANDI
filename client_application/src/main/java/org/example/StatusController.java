package org.example;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
public class StatusController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/")
    public String index(Model model) {
        // Get all statuses from the API Gateway
        List<StatusResponse> statuses = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/statuses")
                .retrieve()
                .bodyToFlux(StatusResponse.class)
                .collectList()
                .block();
        model.addAttribute("statuses", statuses);
        return "index";
    }

    @PostMapping("/setStatus")
    public String setStatus(@RequestParam String username, @RequestParam String statustext, Model model) {
        if (statustext.equalsIgnoreCase("off")) {
            // Remove statuses of the user if statustext is "off"
            String response = webClientBuilder.build()
                    .delete()
                    .uri("http://localhost:8080/api/removeStatus?username=" + username)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            model.addAttribute("message", response);
        } else {
            // Send status update to the API Gateway
            StatusRequest statusRequest = new StatusRequest(username, statustext);
            String response = webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8080/api/setStatus")
                    .bodyValue(statusRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            model.addAttribute("message", response);
        }

        // Refresh the statuses after posting a new one
        List<StatusResponse> statuses = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/statuses")
                .retrieve()
                .bodyToFlux(StatusResponse.class)
                .collectList()
                .block();

        model.addAttribute("statuses", statuses);
        return "index";
    }

    @PostMapping("/getStatus")
    public String getStatus(@RequestParam String username, Model model) {
        // Get status of a specific user from the API Gateway
        StatusResponse status = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/status/" + username)
                .retrieve()
                .bodyToMono(StatusResponse.class)
                .block();
        if (status != null) {
            model.addAttribute("userStatus", status);
        } else {
            model.addAttribute("errorMessage", "User not found");
        }
        // Refresh the statuses
        List<StatusResponse> statuses = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/statuses")
                .retrieve()
                .bodyToFlux(StatusResponse.class)
                .collectList()
                .block();
        model.addAttribute("statuses", statuses);
        return "index";
    }

    @Getter
    @Setter
    public static class StatusRequest {
        private String username;
        private String statustext;

        public StatusRequest(String username, String statustext) {
            this.username = username;
            this.statustext = statustext;
        }
    }

    @Getter
    @Setter
    public static class StatusResponse {
        private String username;
        private String statustext;
        private String timestamp;
        private String formattedTimestamp;
    }
}
