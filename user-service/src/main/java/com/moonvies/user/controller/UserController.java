package com.moonvies.user.controller;

import com.moonvies.user.dto.UserDTO;
import com.moonvies.user.dto.WatchHistoryDTO;
import com.moonvies.user.dto.WatchlistDTO;
import com.moonvies.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --- User CRUD ---
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // --- Watchlist ---
    @PostMapping("/{userId}/watchlist/{videoId}")
    public ResponseEntity<WatchlistDTO> addToWatchlist(@PathVariable Long userId, @PathVariable Long videoId) {
        return ResponseEntity.ok(userService.addToWatchlist(userId, videoId));
    }

    @DeleteMapping("/{userId}/watchlist/{videoId}")
    public ResponseEntity<Void> removeFromWatchlist(@PathVariable Long userId, @PathVariable Long videoId) {
        userService.removeFromWatchlist(userId, videoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/watchlist")
    public ResponseEntity<List<WatchlistDTO>> getUserWatchlist(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserWatchlist(userId));
    }

    // --- Watch History ---
    @PostMapping("/{userId}/history/{videoId}")
    public ResponseEntity<WatchHistoryDTO> recordWatchHistory(
            @PathVariable Long userId,
            @PathVariable Long videoId,
            @RequestParam Integer progressTime,
            @RequestParam Boolean completed) {
        return ResponseEntity.ok(userService.recordWatchHistory(userId, videoId, progressTime, completed));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<WatchHistoryDTO>> getUserWatchHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserWatchHistory(userId));
    }

    @GetMapping("/{userId}/statistics")
    public ResponseEntity<Long> getTotalWatchTime(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getTotalWatchTime(userId));
    }
}
