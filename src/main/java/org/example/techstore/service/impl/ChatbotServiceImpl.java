package org.example.techstore.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.ChatbotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Override
    public String chatWithTechStoreAI(String userMessage) {
        // 1. KỸ NGHỆ PROMPT (Tẩy não AI)
        String systemPrompt = "Bạn là chuyên viên tư vấn bán hàng của cửa hàng công nghệ TechStore. "
                + "Quy tắc cốt lõi của bạn: "
                + "1. Chỉ tư vấn về đồ công nghệ (Điện thoại, Laptop, Phụ kiện). Nếu khách hỏi thời tiết, chính trị, làm toán... hãy từ chối khéo. "
                + "2. Thái độ nhiệt tình, xưng 'em' và gọi khách là 'dạ, anh/chị'. "
                + "3. Trả lời ngắn gọn, súc tích, tối đa 3-4 câu, có dùng emoji cho thân thiện. "
                + "Dưới đây là tin nhắn của khách hàng: [" + userMessage + "]. Hãy trả lời khách ngay lập tức.";

        // 2. GỌI API GEMINI
        return callGeminiApi(systemPrompt);
    }

    private String callGeminiApi(String prompt) {
        try {
            String requestUrl = geminiApiUrl + "?key=" + geminiApiKey;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Xử lý chuỗi để tránh lỗi format JSON khi có dấu ngoặc kép hoặc xuống dòng
            String safePrompt = prompt.replace("\"", "\\\"").replace("\n", " ");
            String requestBody = "{\"contents\": [{\"parts\": [{\"text\": \"" + safePrompt + "\"}]}]}";

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(requestUrl, request, String.class);

            // Parse JSON để lấy đúng phần text câu trả lời của AI
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        } catch (Exception e) {
            log.error("Lỗi khi gọi API Chatbot Gemini: ", e);
            return "Dạ hệ thống CSKH bên em đang bảo trì một chút, anh/chị vui lòng thử lại sau ít phút nhé ạ! 😭";
        }
    }
}
