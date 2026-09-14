package com.devshowcase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Feedback {

    public Feedback() {
    }

    public Feedback(String comment, Project project) {
        this.comment = comment;
        this.project = project;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    //Um Feedback pertence a um Project
    @ManyToOne
    private Project project;
}
