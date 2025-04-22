package com.seph_worker.worker.repository.Notifications;


import com.seph_worker.worker.core.entity.Notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query(value = """
SELECT
    tn.id
FROM type_notification tn
WHERE tn.name=:name
""", nativeQuery = true)
    Integer getIdByName(String name);

}
