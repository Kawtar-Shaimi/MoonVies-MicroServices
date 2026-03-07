package com.moonvies.video.entity;

import com.moonvies.video.entity.enums.VideoCategory;
import com.moonvies.video.entity.enums.VideoType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String thumbnailUrl;
    private String trailerUrl; // YouTube URL
    
    private Integer duration; // in minutes
    private Integer releaseYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoCategory category;

    private Double rating;
    private String director;
    private String cast;
}
