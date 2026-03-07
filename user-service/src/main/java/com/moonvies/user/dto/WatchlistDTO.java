package com.moonvies.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistDTO {
    private Long id;
    private Long userId;
    private Long videoId;
    private VideoDTO video; // Fetched via Feign
    private LocalDateTime addedAt;
}
