package gr.codingschool.iwg.service;

import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> findHistoryByUser(User user) {
        return eventRepository.findAllByUser(user);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
