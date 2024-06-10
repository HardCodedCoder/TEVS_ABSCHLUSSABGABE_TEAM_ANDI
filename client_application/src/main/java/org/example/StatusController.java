package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class StatusController {

    private List<Status> statusList = new ArrayList<>();

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("statuses", statusList);
        return "index";
    }

    @PostMapping("/setStatus")
    public String setStatus(@RequestParam String username, @RequestParam String statusText) {
        updateOrAddStatus(username, statusText);
        return "redirect:/";
    }

    private void updateOrAddStatus(String username, String statusText) {
        for (Iterator<Status> iterator = statusList.iterator(); iterator.hasNext();) {
            Status status = iterator.next();
            if (status.getUsername().equals(username)) {
                if (statusText.equalsIgnoreCase("off")) {
                    iterator.remove();
                } else {
                    status.setStatusText(statusText);
                    status.setTimestamp(LocalDateTime.now());
                }
                return;
            }
        }
        if (!statusText.equalsIgnoreCase("off")) {
            statusList.add(new Status(username, statusText));
        }
    }

}
