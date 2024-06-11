package org.example;

import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.example.StatusController.Status;

@Component
public class StatusListener {

    private final List<StatusController.Status> statuses = new CopyOnWriteArrayList<>();

    @Value("${server.node-id}")
    private String nodeId;

    @RabbitListener(queues = "#{statusQueue.name}")
    public void receiveMessage(StatusController.Status newStatus) {
        synchronized (statuses) {
            if (newStatus.getStatustext().equalsIgnoreCase("off")) {
                statuses.removeIf(status -> status.getUsername().equals(newStatus.getUsername()));
            } else {
                statuses.removeIf(status -> status.getUsername().equals(newStatus.getUsername())
                        && status.getTimestamp().isBefore(newStatus.getTimestamp()));
                statuses.add(newStatus);
            }

            System.out.println("Status received in Listener: " + newStatus.getUsername());
        }
    }

    public void addStatuses(List<StatusController.Status> newStatuses) {
        synchronized (statuses) {
            for (StatusController.Status newStatus : newStatuses) {
                statuses.removeIf(status -> status.getUsername().equals(newStatus.getUsername())
                        && status.getTimestamp().isBefore(newStatus.getTimestamp()));
                statuses.add(newStatus);
            }
        }
    }

    public List<StatusController.Status> getStatuses() {
        return new ArrayList<>(statuses);
    }
}
