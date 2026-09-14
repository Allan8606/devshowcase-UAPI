package com.devshowcase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Technology {

    public Technology() {
    }

    public Technology( String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //uma tecnologia pode estar em vários projetos e um projeto pode usar várias tecnologias.
    @ManyToMany(mappedBy = "technologies")//Tem relação com o tecnhnologies.
    private Set<Project> projects = new HashSet<>();


}
