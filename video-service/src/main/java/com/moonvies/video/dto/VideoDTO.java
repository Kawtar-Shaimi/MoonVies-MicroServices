package com.moonvies.video.dto;

import com.moonvies.video.entity.enums.VideoCategory;
import com.moonvies.video.entity.enums.VideoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDTO {

    private Long id;
    
    @NotBlank(message = "Title is mandatory")
    private String title;
    
    private String description;
    private String thumbnailUrl;
    private String trailerUrl;
    
    private Integer duration;
    private Integer releaseYear;
    
    @NotNull(message = "Type is mandatory")
    private VideoType type;
    
    @NotNull(message = "Category is mandatory")
    private VideoCategory category;
    
    private Double rating;
    private String director;
    private String cast;
}
