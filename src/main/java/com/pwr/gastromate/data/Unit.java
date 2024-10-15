package com.pwr.gastromate.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "units", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"}))
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String abbreviation;

    private Double conversionToGrams;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "unit")
    @JsonBackReference
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "unit")
    @JsonBackReference
    private List<MenuItemIngredient> menuItemIngredients;

}
