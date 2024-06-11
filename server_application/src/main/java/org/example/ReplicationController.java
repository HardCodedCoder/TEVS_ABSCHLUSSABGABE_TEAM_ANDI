package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replication")
public class ReplicationController {

    @Autowired
    private StatusListener statusListener;

    @GetMapping("/statuses")
    public List<StatusController.Status> getStatuses() {
        return statusListener.getStatuses();
    }

    @PostMapping("/statuses")
    public void replicateStatuses(@RequestBody List<StatusController.Status> newStatuses) {
        statusListener.addStatuses(newStatuses);
    }
}
