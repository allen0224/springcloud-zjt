package com.meorient.mebuyerdiscovery.elasticsearch.statistics;

import java.math.BigDecimal;

public class ProductVlUsd {

    public BigDecimal productVlUsdCif;
    public BigDecimal productVlUsd;
    public BigDecimal productVlUsdFob;

    public BigDecimal getProductVlUsdCif() {
        return productVlUsdCif;
    }

    public void setProductVlUsdCif(BigDecimal productVlUsdCif) {
        this.productVlUsdCif = productVlUsdCif;
    }

    public BigDecimal getProductVlUsd() {
        return productVlUsd;
    }

    public void setProductVlUsd(BigDecimal productVlUsd) {
        this.productVlUsd = productVlUsd;
    }

    public BigDecimal getProductVlUsdFob() {
        return productVlUsdFob;
    }

    public void setProductVlUsdFob(BigDecimal productVlUsdFob) {
        this.productVlUsdFob = productVlUsdFob;
    }
}
