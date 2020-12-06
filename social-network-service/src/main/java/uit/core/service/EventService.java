package uit.core.service;

import com.google.api.services.calendar.model.Event;
import org.springframework.stereotype.Service;
import uit.core.calendar.CalendarConfig;
import uit.core.dto.request.EventRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class EventService {
    public Event create(EventRequest eventRequest) throws IOException, GeneralSecurityException {
        return CalendarConfig.createEvent(eventRequest);
    }
}
