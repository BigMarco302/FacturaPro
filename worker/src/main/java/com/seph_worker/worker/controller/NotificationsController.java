package com.seph_worker.worker.controller;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.model.IconDTO;
import com.seph_worker.worker.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/notifications")
public class NotificationsController {

    private final NotificationsService notificationsService;

    @GetMapping("")
    public WebServiceResponse getNotifications(@RequestHeader("userId") Integer userId) {return (notificationsService.getNotificationsByUserId(userId));}

    @PostMapping("")
    public WebServiceResponse addNotification(@RequestHeader("userId") Integer userId, @RequestHeader List<Integer> typesIds) {return notificationsService.subcribeUserToNotification(userId, typesIds);}

    @PatchMapping("/status")
    public WebServiceResponse changeStatusNotification(
            @RequestHeader("status") Integer status, @RequestHeader("notificationId") Integer notificationId){
        return new WebServiceResponse(notificationsService.changeStatusNotification(status,notificationId));
    }

    @PostMapping("/icons")
    public WebServiceResponse addIcon(@RequestBody IconDTO iconDTO){
        return (notificationsService.addIcon(iconDTO));
    }
}
