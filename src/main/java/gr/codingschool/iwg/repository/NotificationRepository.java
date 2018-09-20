package gr.codingschool.iwg.repository;

import gr.codingschool.iwg.model.Notification;
import gr.codingschool.iwg.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);
    List<Notification> findAllByUserAndRead(User user, boolean read);
    Notification findById(int id);
    Notification save(Notification notification);
    int deleteById(int id);
}
