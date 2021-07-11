package com.app.trade.calc.engine.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "TRADE")
@Getter @Setter
public class Trade implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private String stock;
    private String type;
    private Double quantity;
    private Double price;
}
