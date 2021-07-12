package com.app.trade.calc.engine.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "CAPITAL")
@Getter @Setter
public class Capital implements Serializable {

    @Id
    private String account;
    private Double capital;

    @Transient
    private Double targetMarketValue;

    @OneToMany(mappedBy = "capital", fetch = FetchType.LAZY)
    List<Target> targetList;

    @OneToMany(mappedBy = "capital", fetch = FetchType.LAZY)
    List<Holding> holdingList;
}
