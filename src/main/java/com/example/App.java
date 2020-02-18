package com.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableScheduling
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @Scheduled(fixedRate = 1000)
    public Object time() throws Exception {
        // 发现消息
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messagingTemplate.convertAndSend("/topic/time", df.format(new Date()));
        return "time";
    }

    @Scheduled(fixedRate = 2000)
    @SendToUser("/greetings")
    public String greeting2() {
        messagingTemplate.convertAndSendToUser("2", "/greetings", "欢迎您，用户: 2");
        return "OK";
    }

    @Scheduled(fixedRate = 2000)
    @SendToUser("/greetings")
    public String greeting1() {
        messagingTemplate.convertAndSendToUser("1", "/greetings", "欢迎您，用户: 1");
        return "OK";
    }

    @Scheduled(fixedRate = 9000)
    public Object notification() {
        // 发现消息
        messagingTemplate.convertAndSend("/topic/notification", "hello world!");
        return "ok";
    }
}
