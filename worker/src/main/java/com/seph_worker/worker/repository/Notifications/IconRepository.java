package com.seph_worker.worker.repository.Notifications;


import com.seph_worker.worker.core.entity.Notifications.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends JpaRepository<Icon, Integer> {



    @Query(value = """
SELECT
    ino.id
FROM icon_notification ino
WHERE ino.name=:name
""", nativeQuery = true)
    Integer getIdByName(String name);
}

