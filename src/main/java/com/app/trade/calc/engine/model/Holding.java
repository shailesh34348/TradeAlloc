package com.app.trade.calc.engine.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import java.io.Serializable;

@Entity
@Table(name = "HOLDING")
@Getter @Setter
public class Holding implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private String account;
    private String stock;
    private Double quantity;
    private float price;
    private float marketValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", insertable = false, updatable = false)
    private Capital capital;
}
