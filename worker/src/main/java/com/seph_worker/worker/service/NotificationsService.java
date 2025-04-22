package com.seph_worker.worker.service;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.core.entity.Notifications.Icon;
import com.seph_worker.worker.core.entity.Notifications.Message;
import com.seph_worker.worker.core.entity.Notifications.SubscriptionNotificationUser;
import com.seph_worker.worker.core.entity.Notifications.UserNotification;
import com.seph_worker.worker.core.exception.ResourceNotFoundException;
import com.seph_worker.worker.model.IconDTO;
import com.seph_worker.worker.repository.Notifications.*;
import com.seph_worker.worker.repository.UserRoleModule.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;

@Service
@Log4j2
@AllArgsConstructor
public class NotificationsService {

    private IconRepository iconNotificationRepository;
    private NotificationRepository typeNotificationRepository;
    private SubscriptionNotificationUserRepository subscriptionUserNotificationRepository;
    private MessageRepository notificationsRepository;
    private IconRepository iconRepository;
    private UserNotificationRepository userNotificationRepository;
    private UserRepository userRepository;

    @Transactional
    public void notification(String message, String title, String icon, String typeSubscription) {
        Integer type_notification_id = typeNotificationRepository.getIdByName(typeSubscription.toLowerCase());
        if(type_notification_id == null ) {
            throw new ResourceNotFoundException("No existe el tipo de notificacion");
        }
        Message notifications = new Message();
        notifications.setMessage(message);
        notifications.setTitle(title);
        //notifications.setFecha(LocalDateTime.now());
        notifications.setIconId(iconNotificationRepository.getIdByName(icon));
       // notifications.setTypeNotificationId(type_notification_id);
        notificationsRepository.save(notifications);

        List<Integer> usersId = subscriptionUserNotificationRepository.getUsersIdByTypeId(type_notification_id);
        usersId.forEach(userId -> {
            UserNotification userNotification = new UserNotification();
//            userNotification.setUserId(userId);
//            userNotification.setNotificationId(notifications.getId());
            userNotification.setDeleted(FALSE);
            userNotification.setStatus(1);
            userNotificationRepository.save(userNotification);
        });

    }

    @Transactional
    public WebServiceResponse subcribeUserToNotification(Integer userId , List<Integer> typeSubscription){
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No existe el usuario"));

        typeSubscription.forEach(typeId->{
        typeNotificationRepository.findById(typeId).orElseThrow(() -> new ResourceNotFoundException("No existe el tipo de notificacion"));
        SubscriptionNotificationUser subscriptionUserNotification = new SubscriptionNotificationUser();
        subscriptionUserNotification.setUserId(userId);
        //subscriptionUserNotification.setTypeNotificationId(typeId);
        subscriptionUserNotification.setDeleted(FALSE);
        subscriptionUserNotificationRepository.save(subscriptionUserNotification);
        });
        return new WebServiceResponse("Se guardo correctamente el usuario: "+userId);
    }


    public WebServiceResponse getNotificationsByUserId(Integer userId){
        List<Map<String,Object>> notifications = userNotificationRepository.getNotificationByUserId(userId);
        if(notifications.isEmpty()) {
            return new WebServiceResponse(true,"0");
        }
        return new WebServiceResponse(true, String.valueOf(notifications.size()), notifications);
    }

    @Transactional
    public boolean changeStatusNotification(Integer status, Integer notificationId){
        UserNotification noti = userNotificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("No existe la notificacion"));
        noti.setStatus(status);

        try {
            userNotificationRepository.save(noti);
        } catch (Exception e) {
            throw new ResourceNotFoundException("OCURRIO UN ERROR");
        }
        return true;
    }

    @Transactional
    public WebServiceResponse addIcon (IconDTO iconDTO){
        Icon newIcon = new Icon();
        try {
            newIcon.setDescription(iconDTO.getDescription());
            newIcon.setName(iconDTO.getName());
            newIcon.setIcon(iconDTO.getIcon());
            iconRepository.save(newIcon);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Ocurrio un error al guardar el icono");
        }
        return new WebServiceResponse(true,"Se agrego correctamente el icono", "id", newIcon.getId());
    }
}
