package com.moonvies.user.service;

import com.moonvies.user.dto.UserDTO;
import com.moonvies.user.dto.VideoDTO;
import com.moonvies.user.dto.WatchlistDTO;
import com.moonvies.user.entity.User;
import com.moonvies.user.entity.Watchlist;
import com.moonvies.user.feign.VideoClient;
import com.moonvies.user.mapper.UserMapper;
import com.moonvies.user.repository.UserRepository;
import com.moonvies.user.repository.WatchHistoryRepository;
import com.moonvies.user.repository.WatchlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private WatchHistoryRepository watchHistoryRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private VideoClient videoClient;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void createUser_ShouldReturnSavedUser() {
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        UserDTO savedUser = userService.createUser(userDTO);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void addToWatchlist_ShouldReturnWatchlistWithVideoDetails() {
        Long videoId = 100L;
        VideoDTO videoDTO = VideoDTO.builder().id(videoId).title("Test Video").build();
        
        Watchlist watchlist = Watchlist.builder().id(1L).user(user).videoId(videoId).build();
        WatchlistDTO watchlistDTO = WatchlistDTO.builder().id(1L).userId(1L).videoId(videoId).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(videoClient.getVideoById(videoId)).thenReturn(videoDTO);
        when(watchlistRepository.save(any(Watchlist.class))).thenReturn(watchlist);
        when(userMapper.toDto(any(Watchlist.class))).thenReturn(watchlistDTO);

        WatchlistDTO result = userService.addToWatchlist(1L, videoId);

        assertNotNull(result);
        assertEquals(videoId, result.getVideoId());
        assertEquals("Test Video", result.getVideo().getTitle());
        verify(videoClient, times(1)).getVideoById(videoId);
        verify(watchlistRepository, times(1)).save(any(Watchlist.class));
    }
}
