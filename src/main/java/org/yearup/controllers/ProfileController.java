package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private ProfileService profileService;
    private UserService userService;


    public ProfileController(ProfileService profileService, UserService userService) {

        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsersProfile(Principal principal) {

        // get the currently logged in username
        String userName = principal.getName();
        // find database user by username
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        Profile profiles = profileService.getProfileById(userId);

        return ResponseEntity.ok(profiles);
    }
    @PutMapping
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody Profile profile) {



        return ResponseEntity.ok(profile);
    }


}
