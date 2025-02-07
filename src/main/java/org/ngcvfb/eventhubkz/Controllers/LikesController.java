package org.ngcvfb.eventhubkz.Controllers;

import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Services.EventLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
public class LikesController {

    private final EventLikeService likeService;

    public LikesController(EventLikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<String> like(@PathVariable("eventId") Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel currentUser = (UserModel) authentication.getPrincipal();
        String status = likeService.addLike(eventId, currentUser.getId());
        return ResponseEntity.ok(status);
    }


}
