package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class ClientApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ClientApplication.class, args);
    }

    public String index() {
        return "index";
    }

    public String setStatus(@RequestParam String username, @RequestParam String statusText) {
        // TODO: Logic to send status to API Gateway
        return "redirect:/";
    }
}
