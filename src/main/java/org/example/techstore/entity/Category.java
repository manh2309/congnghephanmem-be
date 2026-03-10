package org.example.techstore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.techstore.config.AutoCodeListener;
import org.example.techstore.config.AutoGenerateCode;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AutoCodeListener.class)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_code")
    @AutoGenerateCode(prefix = "CT_", length = 8)
    private String categoryCode;

    @Column(nullable = false)
    private String name;

    private String description;
}
