package org.example.techstore.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.ProductDetail;
import org.example.techstore.repository.ProductDetailRepository;
import org.example.techstore.repository.ProductRepository;
import org.example.techstore.service.ChatbotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final RestTemplate restTemplate;

    private final ProductDetailRepository productDetailRepository;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Override
    public String chatWithTechStoreAI(String userMessage) {

        List<ProductDetail> productDetails = productDetailRepository.findAll();

        DecimalFormat formatter = new DecimalFormat("###,###,###");

        String productCatalog = productDetails.stream()
                .map(pd -> {
                    String productName = pd.getProduct() != null ? pd.getProduct().getName() : "Sản phẩm";
                    String configName = pd.getConfiguration() != null ? pd.getConfiguration().getName() : "Tiêu chuẩn";
                    BigDecimal price = pd.getPrice() != null ? pd.getPrice() : BigDecimal.ZERO;
                    Integer quantity = pd.getQuantity() != null ? pd.getQuantity() : 0;

                    // Ví dụ: "- iPhone 15 Pro Max (Bản 256GB Titan) | Giá: 29,000,000 VNĐ | Tồn kho: 20 chiếc"
                    return String.format("- %s (%s) | Giá: %s VNĐ | Tồn kho: %d chiếc",
                            productName, configName, formatter.format(price), quantity);
                })
                .collect(Collectors.joining("\n"));

        // 🛑 3. KỸ NGHỆ PROMPT (Bơm Data thật vào cho AI)
        String systemPrompt = "Bạn là nhân viên tư vấn bán hàng xuất sắc của cửa hàng công nghệ TechStore.\n"
                + "Quy tắc cốt lõi của bạn:\n"
                + "1. CHỈ TƯ VẤN DỰA TRÊN DANH SÁCH SẢN PHẨM TRONG KHO BÊN DƯỚI. KHÔNG bịa ra cấu hình hoặc mức giá khác.\n"
                + "2. Khi khách hỏi ngân sách (VD: 'Tôi có 20 triệu'), hãy chủ động lọc và gợi ý các máy có GIÁ <= Ngân sách của khách.\n"
                + "3. Nếu khách hỏi mua máy có Tồn kho = 0, hãy báo hết hàng và gợi ý cấu hình khác hoặc máy khác tương đương.\n"
                + "4. Xưng 'em', gọi khách là 'dạ, anh/chị', trả lời tối đa 3-4 câu, dùng emoji thân thiện, xuống dòng dễ nhìn.\n\n"
                + "=== KHO HÀNG HIỆN TẠI CỦA TECHSTORE ===\n"
                + productCatalog + "\n"
                + "=======================================\n\n"
                + "LỊCH SỬ TRÒ CHUYỆN GẦN ĐÂY (JSON):\n"
                + userMessage + "\n\n"
                + "Dựa vào Kho hàng và Lịch sử trò chuyện trên, hãy viết câu trả lời tiếp theo của bạn (chỉ trả về text):";

        // 4. GỌI API GEMINI
        return callGeminiApi(systemPrompt);
    }

    private String callGeminiApi(String prompt) {
        try {
            String requestUrl = geminiApiUrl + "?key=" + geminiApiKey;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper mapper = new ObjectMapper();
            String requestBody = "{\"contents\": [{\"parts\": [{\"text\": " + mapper.writeValueAsString(prompt) + "}]}]}";

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(requestUrl, request, String.class);

            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        } catch (Exception e) {
            log.error("Lỗi khi gọi API Chatbot Gemini: ", e);
            return "Dạ hệ thống CSKH bên em đang bảo trì một chút, anh/chị vui lòng thử lại sau ít phút nhé ạ! 😭";
        }
    }
}
