package com.moonvies.user.feign;

import com.moonvies.user.dto.VideoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "video-service")
public interface VideoClient {

    @GetMapping("/videos/{id}")
    VideoDTO getVideoById(@PathVariable("id") Long id);
}
