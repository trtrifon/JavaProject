package gr.codingschool.iwg.service;

import gr.codingschool.iwg.model.Notification;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> findNotificationsByUser(User user) {
        return notificationRepository.findAllByUser(user);
    }

    public List<Notification> findUnreadNotificationsByUser(User user) {
        return notificationRepository.findAllByUserAndRead(user, false);
    }

    public Notification readNotification(int id){
        Notification notification = notificationRepository.findById(id);
        notification.setRead(true);
        notification = notificationRepository.save(notification);
        return notification;
    }

    public Notification unreadNotification(int id){
        Notification notification = notificationRepository.findById(id);
        notification.setRead(false);
        notification = notificationRepository.save(notification);
        return notification;
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
