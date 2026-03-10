package org.example.techstore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.techstore.config.AutoCodeListener;
import org.example.techstore.config.AutoGenerateCode;

import java.util.List;

@Entity
@Table(name = "configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AutoCodeListener.class)
public class Configuration extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @AutoGenerateCode(prefix = "CONF_", length = 8)
    private String configurationCode;
    // Không unique
    @Column(nullable = false)
    private String name;

    // Cho phép null
    @Column(nullable = true)
    private String description;

    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Specification> specifications;

}
