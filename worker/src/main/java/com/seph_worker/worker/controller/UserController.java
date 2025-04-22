package com.seph_worker.worker.controller;


import com.seph_worker.worker.core.dto.WebServiceResponse;

import com.seph_worker.worker.model.UserDTO;
import com.seph_worker.worker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public WebServiceResponse getAllUser(){
        return new WebServiceResponse(userService.getAllUsers());
    }
    @GetMapping("/user")
    public WebServiceResponse getUser(@RequestHeader Integer userId){
        return new WebServiceResponse(userService.getUser(userId));
    }

    @GetMapping("/moduleByUser")
    public WebServiceResponse getCredentialsByUser(
            @RequestHeader Integer userId){
        return new WebServiceResponse(userService.getCredentialsByUser(userId));
    }

    //POST------------------------------->
    @PostMapping("")
    public WebServiceResponse addUser(@RequestBody UserDTO userDTO){
        return (userService.addUser(userDTO));
    }


    //UPDATE------------------------------->
    @PatchMapping("")
    public WebServiceResponse updateUser(@RequestBody UserDTO userDTO, @RequestHeader Integer userId){return (userService.updateUser(userDTO, userId));}

    @PatchMapping("/softdeleted")
    public WebServiceResponse softdeleted( @RequestHeader Integer userId){return (userService.softdeleted( userId));}
}
