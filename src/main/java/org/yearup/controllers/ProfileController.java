package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.models.Profile;
import org.yearup.service.ProfileService;

import java.util.List;

@RestController
@RequestMapping("profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private ProfileService profileService;


    public ProfileController(ProfileService profileService) {

        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<?> getUsersProfiles() {

        List<Profile> profiles = profileService.getProfiles();

        return ResponseEntity.ok(profiles);
    }

}
