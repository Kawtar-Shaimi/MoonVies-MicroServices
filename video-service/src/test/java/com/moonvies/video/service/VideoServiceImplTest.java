package com.moonvies.video.service;

import com.moonvies.video.dto.VideoDTO;
import com.moonvies.video.entity.Video;
import com.moonvies.video.entity.enums.VideoCategory;
import com.moonvies.video.entity.enums.VideoType;
import com.moonvies.video.mapper.VideoMapper;
import com.moonvies.video.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoMapper videoMapper;

    @InjectMocks
    private VideoServiceImpl videoService;

    private Video video;
    private VideoDTO videoDTO;

    @BeforeEach
    void setUp() {
        video = Video.builder()
                .id(1L)
                .title("Inception")
                .type(VideoType.FILM)
                .category(VideoCategory.SCIENCE_FICTION)
                .build();

        videoDTO = VideoDTO.builder()
                .id(1L)
                .title("Inception")
                .type(VideoType.FILM)
                .category(VideoCategory.SCIENCE_FICTION)
                .build();
    }

    @Test
    void createVideo_ShouldReturnSavedVideo() {
        when(videoMapper.toEntity(any(VideoDTO.class))).thenReturn(video);
        when(videoRepository.save(any(Video.class))).thenReturn(video);
        when(videoMapper.toDto(any(Video.class))).thenReturn(videoDTO);

        VideoDTO savedVideo = videoService.createVideo(videoDTO);

        assertNotNull(savedVideo);
        assertEquals("Inception", savedVideo.getTitle());
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void getVideoById_ShouldReturnVideo_WhenVideoExists() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(videoMapper.toDto(video)).thenReturn(videoDTO);

        VideoDTO foundVideo = videoService.getVideoById(1L);

        assertNotNull(foundVideo);
        assertEquals(1L, foundVideo.getId());
        verify(videoRepository, times(1)).findById(1L);
    }

    @Test
    void getVideoById_ShouldThrowException_WhenVideoDoesNotExist() {
        when(videoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> videoService.getVideoById(2L));
        verify(videoRepository, times(1)).findById(2L);
    }

    @Test
    void getAllVideos_ShouldReturnListOfVideos() {
        when(videoRepository.findAll()).thenReturn(Arrays.asList(video));
        when(videoMapper.toDto(video)).thenReturn(videoDTO);

        List<VideoDTO> videos = videoService.getAllVideos();

        assertNotNull(videos);
        assertEquals(1, videos.size());
        verify(videoRepository, times(1)).findAll();
    }

    @Test
    void updateVideo_ShouldReturnUpdatedVideo() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(videoRepository.save(any(Video.class))).thenReturn(video);
        when(videoMapper.toDto(any(Video.class))).thenReturn(videoDTO);

        VideoDTO updatedVideo = videoService.updateVideo(1L, videoDTO);

        assertNotNull(updatedVideo);
        assertEquals("Inception", updatedVideo.getTitle());
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void deleteVideo_ShouldCallDelete() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        doNothing().when(videoRepository).delete(video);

        videoService.deleteVideo(1L);

        verify(videoRepository, times(1)).delete(video);
    }
}
