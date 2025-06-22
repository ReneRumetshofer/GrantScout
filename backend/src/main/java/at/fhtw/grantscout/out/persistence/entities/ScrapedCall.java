package at.fhtw.grantscout.out.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scraped_call")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapedCall {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", nullable = false)
    private Call call;

    @Column(nullable = false)
    private LocalDateTime scrapedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

}
