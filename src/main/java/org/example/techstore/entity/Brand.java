package org.example.techstore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.techstore.config.AutoCodeListener;
import org.example.techstore.config.AutoGenerateCode;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AutoCodeListener.class)
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AutoGenerateCode(prefix = "BR_", length = 8)
    private String brandCode;
    @Column(nullable = false)
    private String name;
}

