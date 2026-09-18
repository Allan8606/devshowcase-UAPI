package com.devshowcase.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Project {

    public Project() {
    }

    public Project(String title, String description,
                   String githubUrl, String projectUrl,
                   Profile profile) {
        this.title = title;
        this.description = description;
        this.githubUrl = githubUrl;
        this.projectUrl = projectUrl;
        this.profile = profile;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String githubUrl;
    private String projectUrl;

    private Double averageRating = 0.0;
    private Integer upvotes = 0;

    // Um Profile pode ter vários Projects.
    @ManyToOne
    private Profile profile;

    // Um projeto pode usar várias tecnologias.
    // Set não permite elementos duplicados.
    @ManyToMany
    private Set<Technology> technologies = new HashSet<>();

    // Um projeto pode receber vários feedbacks.
    @OneToMany(mappedBy = "project")
    private Set<Feedback> feedbacks = new HashSet<>();
}
