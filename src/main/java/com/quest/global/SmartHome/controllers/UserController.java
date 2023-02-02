package com.quest.global.SmartHome.controllers;


import com.quest.global.SmartHome.dtos.UserViewDTO;
import com.quest.global.SmartHome.exceptions.UserNotFoundException;
import com.quest.global.SmartHome.models.ERole;
import com.quest.global.SmartHome.models.Role;
import com.quest.global.SmartHome.models.User;
import com.quest.global.SmartHome.payload.request.SignupRequest;
import com.quest.global.SmartHome.payload.response.MessageResponse;
import com.quest.global.SmartHome.services.RoleService;
import com.quest.global.SmartHome.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {

        List<UserViewDTO> users = userService.listAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody SignupRequest signupRequest) {

        User user = new User(
                signupRequest.getUserName(),
                signupRequest.getEmail(),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getIsRoot(),
                signupRequest.getPassword()
        );

        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findRoleByName(ERole.Admin));

        if (user.getIsRoot()) {
            roles.add(roleService.findRoleByName(ERole.Root));
        }

        user.setRoles(roles);
        userService.createUser(user);

        return ResponseEntity.ok(new MessageResponse("User has been registered!"));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable ObjectId id) throws UserNotFoundException {
        userService.deleteUserByID(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserViewDTO> getById(@PathVariable ObjectId id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserByID(id),
                HttpStatus.OK
        );
    }

}