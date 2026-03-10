package org.example.techstore.config;

import jakarta.persistence.PrePersist;

import java.lang.reflect.Field;
import java.util.UUID;

public class AutoCodeListener {

    @PrePersist
    public void prePersist(Object entity) {
        // Lấy tất cả các field của Entity (kể cả từ class cha như BaseEntity)
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoGenerateCode.class)) {
                AutoGenerateCode annotation = field.getAnnotation(AutoGenerateCode.class);

                try {
                    field.setAccessible(true); // Cho phép truy cập field private
                    Object currentValue = field.get(entity);

                    // Chỉ generate nếu field đang null
                    if (currentValue == null) {
                        String generatedCode = generateCode(annotation.prefix(), annotation.length());
                        field.set(entity, generatedCode);
                    }
                } catch (IllegalAccessException e) {
                    // Log lỗi nếu cần, hoặc ném exception tùy ông
                    throw new RuntimeException("Lỗi khi tự động generate code cho: " + entity.getClass().getSimpleName(), e);
                }
            }
        }
    }

    private String generateCode(String prefix, int length) {
        String nanoPart = String.valueOf(System.nanoTime() % 1000);
        String randomPart = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        String code = randomPart.substring(0, Math.min(length - nanoPart.length(), randomPart.length()));
        return prefix + code + nanoPart;
    }
}
