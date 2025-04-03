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

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Call call;

    @Column(nullable = false)
    private LocalDateTime scrapedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

}
