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
public class EventResponse {

    @JsonProperty("event-id")
    private long id;
    @JsonProperty("event-name")
    private String name;
    @JsonProperty("event-description")
    private String description;

}
