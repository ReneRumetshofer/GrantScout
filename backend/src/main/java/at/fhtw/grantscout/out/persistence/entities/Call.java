package at.fhtw.grantscout.out.persistence.entities;

import at.fhtw.grantscout.core.domain.enums.CallStatus;
import at.fhtw.grantscout.core.domain.enums.Institute;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "call", uniqueConstraints = {@UniqueConstraint(columnNames = "url", name = "UQ_call_url")})
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Institute institute;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CallStatus status;
}
