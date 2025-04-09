package ru.practicum.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lat", "lon"}))
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lat")
    private Float latitude;
    @Column(name = "lon")
    private Float longitude;

    @OneToMany(mappedBy = "location")
    private List<Event> events = new ArrayList<>();
}
