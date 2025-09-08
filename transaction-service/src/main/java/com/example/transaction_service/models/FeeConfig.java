package com.example.transaction_service.models;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "fee_config")
public class FeeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal minFee;

    private BigDecimal maxFee;

    private BigDecimal percentFee; 

 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMinFee() {
        return minFee;
    }

    public void setMinFee(BigDecimal minFee) {
        this.minFee = minFee;
    }

    public BigDecimal getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(BigDecimal maxFee) {
        this.maxFee = maxFee;
    }

    public BigDecimal getPercentFee() {
        return percentFee;
    }

    public void setPercentFee(BigDecimal percentFee) {
        this.percentFee = percentFee;
    }
}
