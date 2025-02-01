package com.example.test.controller;

import com.example.test.service.FileUpdateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    FileUpdateListener fileUpdateListener;

    @MessageMapping("/getLines")
    public String send(@Headers MessageHeaders messageHeaders, @Payload String message, @Header(name = "simpSessionId") String sessionId) throws Exception {
        List<String> fileLastLines = fileUpdateListener.fileLinesQueue.stream().collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for(String line : fileLastLines){
            stringBuilder.append(line).append("</br>");
        }
        simpMessagingTemplate.convertAndSendToUser(sessionId,"/queue/fileLastLines", stringBuilder.toString(), messageHeaders);
        return "done";
    }
}
