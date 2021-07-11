package com.app.trade.calc.engine.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TARGET")
@Getter @Setter
public class Target implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private String account;
    private String stock;
    private float percent;

    @ManyToOne
    @JoinColumn(name = "account", insertable = false, updatable = false)
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    private Capital capital;
}
