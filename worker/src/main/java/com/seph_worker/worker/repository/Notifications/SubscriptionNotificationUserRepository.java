package com.seph_worker.worker.repository.Notifications;


import com.seph_worker.worker.core.entity.Notifications.SubscriptionNotificationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionNotificationUserRepository extends JpaRepository<SubscriptionNotificationUser, Integer> {

    @Query(value = """
SELECT
    sun.user_id
FROM subscription_user_notification sun
WHERE sun.type_notification_id=:typeId
AND sun.deleted = 0 
""", nativeQuery = true)
    List<Integer> getUsersIdByTypeId(Integer typeId);
}
