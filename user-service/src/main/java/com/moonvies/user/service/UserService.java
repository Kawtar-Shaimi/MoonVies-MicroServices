package com.moonvies.user.service;

import com.moonvies.user.dto.UserDTO;
import com.moonvies.user.dto.WatchHistoryDTO;
import com.moonvies.user.dto.WatchlistDTO;

import java.util.List;

public interface UserService {

    // User CRUD
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);

    // Watchlist
    WatchlistDTO addToWatchlist(Long userId, Long videoId);
    void removeFromWatchlist(Long userId, Long videoId);
    List<WatchlistDTO> getUserWatchlist(Long userId);

    // Watch History
    WatchHistoryDTO recordWatchHistory(Long userId, Long videoId, Integer progressTime, Boolean completed);
    List<WatchHistoryDTO> getUserWatchHistory(Long userId);
    
    // Viewing Statistics
    long getTotalWatchTime(Long userId);
}
