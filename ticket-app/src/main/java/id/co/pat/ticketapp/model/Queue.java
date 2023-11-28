package id.co.pat.ticketapp.model;

import id.co.pat.ticketapp.model.enums.QueueStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "queue")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;
    @Enumerated(EnumType.STRING)
    @Column(name = "queue_status", nullable = false)
    private QueueStatus queueStatus;

}
