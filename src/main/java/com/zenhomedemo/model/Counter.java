package com.zenhomedemo.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COUNTER")
@SuperBuilder(toBuilder = true)
public class Counter {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "AMOUNT")
    private double amount;

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "VILLAGE_ID", referencedColumnName = "ID")
    private Village village;
}

