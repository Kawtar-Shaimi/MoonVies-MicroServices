package com.moonvies.user.mapper;

import com.moonvies.user.dto.UserDTO;
import com.moonvies.user.dto.WatchHistoryDTO;
import com.moonvies.user.dto.WatchlistDTO;
import com.moonvies.user.entity.User;
import com.moonvies.user.entity.WatchHistory;
import com.moonvies.user.entity.Watchlist;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        if (user.getWatchlists() != null) {
            dto.setWatchlists(user.getWatchlists().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
        }

        if (user.getWatchHistories() != null) {
            dto.setWatchHistories(user.getWatchHistories().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public WatchlistDTO toDto(Watchlist watchlist) {
        if (watchlist == null) {
            return null;
        }
        return WatchlistDTO.builder()
                .id(watchlist.getId())
                .userId(watchlist.getUser().getId())
                .videoId(watchlist.getVideoId())
                .addedAt(watchlist.getAddedAt())
                .build();
    }

    public WatchHistoryDTO toDto(WatchHistory watchHistory) {
        if (watchHistory == null) {
            return null;
        }
        return WatchHistoryDTO.builder()
                .id(watchHistory.getId())
                .userId(watchHistory.getUser().getId())
                .videoId(watchHistory.getVideoId())
                .watchedAt(watchHistory.getWatchedAt())
                .progressTime(watchHistory.getProgressTime())
                .completed(watchHistory.getCompleted())
                .build();
    }
}
