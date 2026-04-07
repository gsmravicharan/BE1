package SmartAirAndHealthMointoring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/msg")
    public String msg(@RequestBody Map<String, String> body) {
        String messageText = body.get("message");
        Object response = jmsTemplate.sendAndReceive("ai-health-queue", session -> {
            return session.createTextMessage(messageText);
        });

        if (response instanceof jakarta.jms.TextMessage textMessage) {
            try {
                return textMessage.getText();
            } catch (jakarta.jms.JMSException e) {
                return "Error reading response: " + e.getMessage();
            }
        }

        return response != null ? response.toString() : "Error: No response from AI service.";
    }
}