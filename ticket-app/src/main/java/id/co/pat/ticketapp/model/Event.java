package id.co.pat.ticketapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "event")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "event_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String eventName;
    @Column(name = "event_description", columnDefinition = "VARCHAR(1200)", nullable = false)
    private String eventDescription;

}
