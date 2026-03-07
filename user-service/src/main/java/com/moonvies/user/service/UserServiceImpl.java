package com.moonvies.user.service;

import com.moonvies.user.dto.UserDTO;
import com.moonvies.user.dto.VideoDTO;
import com.moonvies.user.dto.WatchHistoryDTO;
import com.moonvies.user.dto.WatchlistDTO;
import com.moonvies.user.entity.User;
import com.moonvies.user.entity.WatchHistory;
import com.moonvies.user.entity.Watchlist;
import com.moonvies.user.feign.VideoClient;
import com.moonvies.user.mapper.UserMapper;
import com.moonvies.user.repository.UserRepository;
import com.moonvies.user.repository.WatchHistoryRepository;
import com.moonvies.user.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WatchlistRepository watchlistRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final UserMapper userMapper;
    private final VideoClient videoClient;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        userRepository.delete(existingUser);
    }

    @Override
    public WatchlistDTO addToWatchlist(Long userId, Long videoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Check if video exists via Feign
        VideoDTO video;
        try {
            video = videoClient.getVideoById(videoId);
        } catch (Exception e) {
            throw new RuntimeException("Video not found or video service unavailable for id: " + videoId);
        }

        Watchlist watchlist = Watchlist.builder()
                .user(user)
                .videoId(videoId)
                .addedAt(LocalDateTime.now())
                .build();
        
        Watchlist saved = watchlistRepository.save(watchlist);
        WatchlistDTO dto = userMapper.toDto(saved);
        dto.setVideo(video); // attach video details
        return dto;
    }

    @Override
    public void removeFromWatchlist(Long userId, Long videoId) {
        Watchlist watchlist = watchlistRepository.findByUserIdAndVideoId(userId, videoId)
                .orElseThrow(() -> new RuntimeException("Watchlist entry not found"));
        watchlistRepository.delete(watchlist);
    }

    @Override
    public List<WatchlistDTO> getUserWatchlist(Long userId) {
        return watchlistRepository.findByUserId(userId).stream()
                .map(w -> {
                    WatchlistDTO dto = userMapper.toDto(w);
                    try {
                        VideoDTO video = videoClient.getVideoById(w.getVideoId());
                        dto.setVideo(video);
                    } catch (Exception e) {
                        System.err.println("Failed to fetch video details: " + e.getMessage());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public WatchHistoryDTO recordWatchHistory(Long userId, Long videoId, Integer progressTime, Boolean completed) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        WatchHistory history = watchHistoryRepository.findByUserIdAndVideoId(userId, videoId)
                .orElse(WatchHistory.builder()
                        .user(user)
                        .videoId(videoId)
                        .build());

        history.setWatchedAt(LocalDateTime.now());
        history.setProgressTime(progressTime);
        history.setCompleted(completed);

        WatchHistory saved = watchHistoryRepository.save(history);
        WatchHistoryDTO dto = userMapper.toDto(saved);
        
        try {
            VideoDTO video = videoClient.getVideoById(videoId);
            dto.setVideo(video);
        } catch (Exception e) {}
        
        return dto;
    }

    @Override
    public List<WatchHistoryDTO> getUserWatchHistory(Long userId) {
        return watchHistoryRepository.findByUserId(userId).stream()
                .map(h -> {
                    WatchHistoryDTO dto = userMapper.toDto(h);
                    try {
                        VideoDTO video = videoClient.getVideoById(h.getVideoId());
                        dto.setVideo(video);
                    } catch (Exception e) {}
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalWatchTime(Long userId) {
        List<WatchHistory> history = watchHistoryRepository.findByUserId(userId);
        return history.stream()
                .filter(h -> h.getProgressTime() != null)
                .mapToInt(WatchHistory::getProgressTime)
                .sum();
    }
}
