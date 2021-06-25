package com.trade_accounting.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventarization")
public class Inventarization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date")
    private LocalDateTime date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;        //почему нельзя сюда сразу дтошку?

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @Column(name = "status")
    @ColumnDefault("false")
    private Boolean status;

    @Column(name = "comment")
    private String comment;

    //LIST inventarization products!!!      //нужно, чтобы выводить по клику на id всю карточку инвентаризации?
}
