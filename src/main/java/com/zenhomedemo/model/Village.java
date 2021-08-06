package com.zenhomedemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VILLAGE")
@JsonIgnoreProperties({"counters"})
public class Village {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="village_seq")
    @SequenceGenerator(initialValue = 1, name = "village_seq", sequenceName = "village_seq", allocationSize = 1)
    private long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL)
    private List<Counter> counters;
}

