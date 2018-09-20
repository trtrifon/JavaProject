package gr.codingschool.iwg.repository;

import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);
    Event save(Event event);
    int deleteById(int id);
}
