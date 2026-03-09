package org.example.techstore.config.configcuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dq8pb7bqt",
                "api_key", "376294185544679",
                "api_secret", "RheAdyeDB5JT5MaAQ6geDvZ-qm4"
        ));
    }
}
