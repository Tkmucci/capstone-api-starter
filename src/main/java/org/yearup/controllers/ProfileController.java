package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;


    public ProfileController(ProfileService profileService, UserService userService) {

        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
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


        User user = userService.getByUserName(principal.getName());
        try{

            Profile updatedProfile = profileService.updateProfile(user.getId(), profile);
            return ResponseEntity.ok(updatedProfile);

        }
        catch (RuntimeException e){

            return ResponseEntity.status(404).body("Could not find the profile");
        }
    }


}
