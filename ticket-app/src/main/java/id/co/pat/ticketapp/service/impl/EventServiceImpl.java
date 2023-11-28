package id.co.pat.ticketapp.service.impl;

import id.co.pat.ticketapp.dto.EventResponse;
import id.co.pat.ticketapp.model.Event;
import id.co.pat.ticketapp.repository.EventRepository;
import id.co.pat.ticketapp.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<EventResponse> getOnGoingEvents() {
        log.info("Getting on going events with last id");
        Iterable<Event> events = eventRepository.findAll();
        return StreamSupport.stream(events.spliterator(), false)
                .map(event -> EventResponse.builder()
                        .id(event.getId())
                        .name(event.getEventName())
                        .description(event.getEventDescription())
                        .build()).toList();
    }

    @Override
    public boolean checkEventExists(long eventId) {
        return eventRepository.existsById(eventId);
    }

}
