package com.example.test.handler;

import com.example.test.service.FileUpdateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StompConnectEvent implements ApplicationListener<SessionSubscribeEvent> {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    FileUpdateListener fileUpdateListener;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {

    }
}
