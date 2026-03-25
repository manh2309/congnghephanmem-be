package org.example.techstore.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AiAnalyticsServiceImplTest {
    @InjectMocks
    private AiAnalyticsServiceImpl aiAnalyticsService;

    @Test
    void testCalculateWMA_ShouldReturnCorrectCeilValue() {
        //ARRANGE (Chuẩn bị)
        int month1 = 10;
        int month2 = 15;
        int month3 = 20;

        // Tính tay: (10*0.2) + (15*0.3) + (20*0.5) = 16.5 -> Làm tròn trần lên 17
        int expectedDemand = 17;

        // 2. ACT (Thực thi hàm)
        int actualDemand = aiAnalyticsService.calculateWMA(month1, month2, month3);

        //
        assertEquals(expectedDemand, actualDemand, "Thuật toán WMA tính toán sai số liệu!");
    }

}
