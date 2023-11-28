package id.co.pat.ticketapp.service;


import id.co.pat.ticketapp.dto.EventResponse;

import java.util.List;

public interface EventService {

    List<EventResponse> getOnGoingEvents();
    boolean checkEventExists(long eventId);

}
