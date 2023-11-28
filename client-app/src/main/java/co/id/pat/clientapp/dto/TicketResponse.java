package co.id.pat.clientapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TicketResponse {

    @JsonProperty("ticket-id")
    private long id;
    @JsonProperty("seat-id")
    private int seatId;
    @JsonProperty("seat-price")
    private double price;

}
