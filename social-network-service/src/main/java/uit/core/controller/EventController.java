package uit.core.controller;

import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.core.dto.request.EventRequest;
import uit.core.service.EventService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping
    public Event create(@RequestBody EventRequest eventRequest) throws IOException, GeneralSecurityException {
        return eventService.create(eventRequest);
    }
}
