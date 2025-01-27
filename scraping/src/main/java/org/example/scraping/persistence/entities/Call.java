package org.example.scraping.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "call", uniqueConstraints = {@UniqueConstraint(columnNames = "url")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "call_id_seq")
    @SequenceGenerator(name = "call_id_seq", sequenceName = "call_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 1024)
    private String url;

    @Column(nullable = false, length = 255)
    private String institute;

    @Column(nullable = false)
    private Boolean scraped = false;

    @Column(columnDefinition = "TEXT")
    private String content;
}
