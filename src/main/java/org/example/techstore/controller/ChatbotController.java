package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.techstore.dto.request.ChatRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.ChatbotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chatbot AI CSKH", description = "API tích hợp Chatbot thông minh tư vấn bán hàng")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    @Operation(summary = "Gửi tin nhắn cho Chatbot", description = "Chatbot AI tự động đóng vai nhân viên TechStore để trả lời khách hàng.")
    public ApiResponse<Object> askChatbot(@RequestBody ChatRequest request) {

        // Gọi Service xử lý
        String aiReply = chatbotService.chatWithTechStoreAI(request.getMessage());

        // Bọc vào ApiResponse chuẩn chỉ


        return  ApiResponse.builder()
                .code(200)
                .message("Phản hồi từ AI TechStore")
                .result(aiReply)
                .build();
    }
}
