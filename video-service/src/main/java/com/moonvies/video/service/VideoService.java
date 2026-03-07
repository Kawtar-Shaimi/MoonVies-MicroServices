package com.moonvies.video.service;

import com.moonvies.video.dto.VideoDTO;
import java.util.List;

public interface VideoService {

    VideoDTO createVideo(VideoDTO videoDTO);
    
    VideoDTO getVideoById(Long id);
    
    List<VideoDTO> getAllVideos();
    
    VideoDTO updateVideo(Long id, VideoDTO videoDTO);
    
    void deleteVideo(Long id);
}
