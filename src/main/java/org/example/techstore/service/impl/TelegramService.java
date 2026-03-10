package org.example.techstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private final RestTemplate restTemplate;

    public void sendOrderMessage(String message) {

        try {

            String url = String.format(
                    "https://api.telegram.org/bot%s/sendMessage",
                    botToken
            );

            Map<String, Object> params = Map.of(
                    "chat_id", chatId,
                    "text", message,
                    "parse_mode", "HTML"
            );

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, params, String.class);

            System.out.println("Telegram response: " + response.getBody());

        } catch (Exception e) {
            System.err.println("Lỗi gửi Telegram: " + e.getMessage());
        }
    }
}
