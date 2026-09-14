package com.devshowcase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Profile {

    public Profile() {
    }

    public Profile(String name, String bio, String githubUrl, String linkedinUrl) {
        this.name = name;
        this.bio = bio;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String bio;
    private String githubUrl;
    private String linkedinUrl;

    // Um perfil possui vários projetos
    @OneToMany(mappedBy = "profile")
    private Set<Project> projects = new HashSet<>();
}
