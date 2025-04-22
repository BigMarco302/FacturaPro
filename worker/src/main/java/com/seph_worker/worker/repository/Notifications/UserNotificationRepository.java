package com.seph_worker.worker.repository.Notifications;


import com.seph_worker.worker.core.entity.Notifications.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {


    @Query(value = """
SELECT
    un.id,
    un.status,
    n.title,
    n.message,
    n.fecha,
    ic.icon,
    tn.name
FROM user_notification un
INNER JOIN notifications n ON un.notification_id = n.id
INNER JOIN icon_notification ic ON n.icon_id = ic.id
INNER JOIN type_notification tn ON n.type_notification_id = tn.id
WHERE un.user_id=:userId
AND un.deleted = 0 
AND un.status = 1
""", nativeQuery = true)
    List<Map<String,Object>> getNotificationByUserId(Integer userId);
}
