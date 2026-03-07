package com.moonvies.video.service;

import com.moonvies.video.dto.VideoDTO;
import com.moonvies.video.entity.Video;
import com.moonvies.video.mapper.VideoMapper;
import com.moonvies.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    @Override
    public VideoDTO createVideo(VideoDTO videoDTO) {
        Video video = videoMapper.toEntity(videoDTO);
        Video savedVideo = videoRepository.save(video);
        return videoMapper.toDto(savedVideo);
    }

    @Override
    public VideoDTO getVideoById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id : " + id));
        return videoMapper.toDto(video);
    }

    @Override
    public List<VideoDTO> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(videoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VideoDTO updateVideo(Long id, VideoDTO videoDTO) {
        Video existingVideo = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id : " + id));
        
        existingVideo.setTitle(videoDTO.getTitle());
        existingVideo.setDescription(videoDTO.getDescription());
        existingVideo.setThumbnailUrl(videoDTO.getThumbnailUrl());
        existingVideo.setTrailerUrl(videoDTO.getTrailerUrl());
        existingVideo.setDuration(videoDTO.getDuration());
        existingVideo.setReleaseYear(videoDTO.getReleaseYear());
        existingVideo.setType(videoDTO.getType());
        existingVideo.setCategory(videoDTO.getCategory());
        existingVideo.setRating(videoDTO.getRating());
        existingVideo.setDirector(videoDTO.getDirector());
        existingVideo.setCast(videoDTO.getCast());
        
        Video updatedVideo = videoRepository.save(existingVideo);
        return videoMapper.toDto(updatedVideo);
    }

    @Override
    public void deleteVideo(Long id) {
        Video existingVideo = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id : " + id));
        videoRepository.delete(existingVideo);
    }
}
