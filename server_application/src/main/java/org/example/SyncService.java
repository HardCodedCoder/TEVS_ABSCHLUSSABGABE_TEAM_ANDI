package org.example;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SyncService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private StatusListener statusListener;

    @PostConstruct
    public void syncStatuses() {
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("server-node");
            if (instances != null && !instances.isEmpty()) {
                // Get the first available instance
                ServiceInstance instance = instances.get(0);
                String existingNodeUrl = instance.getUri().toString() + "/api/replication/statuses";

                List<StatusController.Status> statuses = webClientBuilder.build()
                        .get()
                        .uri(existingNodeUrl)
                        .retrieve()
                        .bodyToFlux(StatusController.Status.class)
                        .collectList()
                        .block();

                if (statuses != null) {
                    statusListener.addStatuses(statuses);
                    System.out.println("Successfully synchronized statuses from existing node.");
                }
            } else {
                System.out.println("No other nodes found. Skipping status synchronization.");
            }
        } catch (Exception e) {
            System.out.println("Error during status synchronization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
