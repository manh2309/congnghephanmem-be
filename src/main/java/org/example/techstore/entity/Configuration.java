package org.example.techstore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Configuration extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String configurationCode;
    // Không unique
    @Column(nullable = false)
    private String name;

    // Cho phép null
    @Column(nullable = true)
    private String description;

    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY)
    private List<Specification> specifications;

}
