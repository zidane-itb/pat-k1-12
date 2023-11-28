package co.id.pat.clientapp.model;

import co.id.pat.clientapp.model.enums.BookingStatus;
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
@Table(name = "ticket_booking_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TicketBookingHistory {

    @Id
    @Column(name = "ticket_booking_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status",nullable = false)
    private BookingStatus bookingStatus;
    @Column(name = "account_id")
    private Long accountId;

}
