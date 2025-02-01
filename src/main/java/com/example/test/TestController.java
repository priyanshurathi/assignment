package com.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/test")
    public ResponseEntity testMethod (@RequestBody String request){
        System.out.println(request);
        simpMessagingTemplate.convertAndSend("/topic/fileUpdates", request);
        return new ResponseEntity("Test123", HttpStatus.OK);
    }

}
