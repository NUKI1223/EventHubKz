package org.ngcvfb.eventhubkz.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ngcvfb.eventhubkz.DTO.EventDTO;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Services.EventLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Likes", description = "Likes API for users and events")
@RestController
@RequestMapping("/api/like")
public class LikesController {

    private final EventLikeService likeService;

    public LikesController(EventLikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "Getting event like count")
    @GetMapping("/{eventId}")
    public ResponseEntity<Integer> getEventLikesCount(@PathVariable Long eventId) {
        return ResponseEntity.ok(likeService.getEventLikeCount(eventId));
    }
    @Operation(summary = "Getting what events user liked")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventDTO>> getUserLikes(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.getEventLikes(userId));
    }

    @Operation(summary = "like or unlike event")
    @PostMapping("/{eventId}")
    public ResponseEntity<String> like(@PathVariable("eventId") Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel currentUser = (UserModel) authentication.getPrincipal();
        String status = likeService.addLike(eventId, currentUser.getId());
        return ResponseEntity.ok(status);
    }


    @Operation(summary = "like or unlike event")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> unlike(@PathVariable("eventId") Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel currentUser = (UserModel) authentication.getPrincipal();
        if (currentUser!=null){
            String status = likeService.deleteLike(eventId, currentUser.getId());
            return ResponseEntity.ok(status);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


}
