package Banking.TransactionService.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Entity
@Table(name = "fee_config")
public class FeeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_fee", nullable = false, precision = 18, scale = 2)
    private BigDecimal minFee;

    @Column(name = "max_fee", nullable = false, precision = 18, scale = 2)
    private BigDecimal maxFee;

    @Column(name = "percent_fee", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentFee;

    // Getters and Setters
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