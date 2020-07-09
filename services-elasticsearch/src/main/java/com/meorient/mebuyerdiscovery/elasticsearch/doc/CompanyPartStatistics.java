package com.meorient.mebuyerdiscovery.elasticsearch.doc;

import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

import static com.meorient.mebuyerdiscovery.elasticsearch.doc.Statistics.COMPANY_PART_INDEX;
import static com.meorient.mebuyerdiscovery.elasticsearch.doc.Statistics.COMPANY_PART_TYPE;


@Document(indexName = COMPANY_PART_INDEX,type =COMPANY_PART_TYPE )
public class CompanyPartStatistics {
    //公司名称
    private String companyName;
    //国家
    private String country;
    //采购编码
    private String code;
    //采购次数
    private Long purchases;
    //次数占比（当前编码采购次数/该公司总次数）*100%
    private Float purchasesRatio;
    //采购金额(usd)
    private BigDecimal purchaseAmountUSD;
    //采购金额占比（当前编码采购金额/该公司总金额）*100%
    private Float purchaseAmountRatio;
    //-采购重量（KG）
    private Double purchaseWeight;
    //-采购重量占比（当前编码采购重量/该公司总重量）*100%
    private Float purchaseWeightRatio;
    //-采购件数
    private Long purchaseNumber;
    //-采购件数占比（当前编码采购件数/该公司总件数）*100%
    private Float purchaseNumberRatio;
    //是否从中国进口
    private Boolean InChina=false;
    //从中国采购次数
    private Long purchasesINChina;
    //从中国采购次数占比（当前编码从中国采购件数/该编码总件数）*100%
    private Float purchasesINChinaRatio;
    //从中国采购金额（USD）
    private BigDecimal PurchaseAmountINChinaUSD;
    //从中国采购金额占比（当前编码从中国采购金额/该编码总金额）*100%
    private BigDecimal PurchaseAmountINChinaUSDRatio;
    //-从中国采购重量（KG）
    private Double PurchaseWeightINChina;
    //-从中国采购重量占比（当前编码从中国采购重量/该编码总重量）*100%
    private Double PurchaseWeightINChinaRatio;
    //-从中国采购件数（KG）
    private Long purchaseNumberINChina;
    //-从中国采购金额占比（当前编码从中国采购件数/该编码总件数）*100%
    private Long purchaseNumberINRatio;
    //贸易客单价USD（该编码贸易金额/该编码贸易次数）
    private BigDecimal traderPrice;

    private BigDecimal traderPriceCif;

    private BigDecimal traderPriceUsd;

    private BigDecimal traderPriceFob;

    //总供应商数
    private Long totalSuppliers;
    //中国供应商数
    private Long chinaSuppliers;
    //中国供应商占比（当前编码的中国供应商数/该编码总供应商数）*100%
    private Float chinaRatio;
    //同行数（指采购同一编码的全球采购商数）
    private Long peer;
    //本国同行数（指采购同一编码的本国采购商）
    private Long domesticPeers;
    //本国同行占比（该编码本国同行/全球同行数）*100%
    private Float domesticPeersRatio;


    private BigDecimal productVlUsdCif;

    private BigDecimal productVlUsd;

    private BigDecimal productVlUsdFob;


    private Float productVlUsdCifRatio;

    private Float productVlUsdRatio;

    private Float productVlUsdFobRatio;


    private Float chinaProductVlUsdCifRatio;

    private Float chinaProductVlUsdRatio;
    private Float chinaProductVlUsdFobRatio;

    public Float getChinaProductVlUsdFobRatio() {
        return chinaProductVlUsdFobRatio;
    }

    public void setChinaProductVlUsdFobRatio(Float chinaProductVlUsdFobRatio) {
        this.chinaProductVlUsdFobRatio = chinaProductVlUsdFobRatio;
    }

    public Float getChinaProductVlUsdCifRatio() {
        return chinaProductVlUsdCifRatio;
    }

    public void setChinaProductVlUsdCifRatio(Float chinaProductVlUsdCifRatio) {
        this.chinaProductVlUsdCifRatio = chinaProductVlUsdCifRatio;
    }

    public Float getChinaProductVlUsdRatio() {
        return chinaProductVlUsdRatio;
    }

    public void setChinaProductVlUsdRatio(Float chinaProductVlUsdRatio) {
        this.chinaProductVlUsdRatio = chinaProductVlUsdRatio;
    }



    public Float getProductVlUsdCifRatio() {
        return productVlUsdCifRatio;
    }

    public void setProductVlUsdCifRatio(Float productVlUsdCifRatio) {
        this.productVlUsdCifRatio = productVlUsdCifRatio;
    }

    public Float getProductVlUsdRatio() {
        return productVlUsdRatio;
    }

    public void setProductVlUsdRatio(Float productVlUsdRatio) {
        this.productVlUsdRatio = productVlUsdRatio;
    }

    public Float getProductVlUsdFobRatio() {
        return productVlUsdFobRatio;
    }

    public void setProductVlUsdFobRatio(Float productVlUsdFobRatio) {
        this.productVlUsdFobRatio = productVlUsdFobRatio;
    }

    private BigDecimal chinaProductVlUsdCif;

    private BigDecimal chinaProductVlUsd;


    private BigDecimal chinaProductVlUsdFob;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPurchases() {
        return purchases;
    }

    public void setPurchases(Long purchases) {
        this.purchases = purchases;
    }

    public Float getPurchasesRatio() {
        return purchasesRatio;
    }

    public void setPurchasesRatio(Float purchasesRatio) {
        this.purchasesRatio = purchasesRatio;
    }

    public BigDecimal getPurchaseAmountUSD() {
        return purchaseAmountUSD;
    }

    public void setPurchaseAmountUSD(BigDecimal purchaseAmountUSD) {
        this.purchaseAmountUSD = purchaseAmountUSD;
    }

    public Float getPurchaseAmountRatio() {
        return purchaseAmountRatio;
    }

    public void setPurchaseAmountRatio(Float purchaseAmountRatio) {
        this.purchaseAmountRatio = purchaseAmountRatio;
    }

    public Double getPurchaseWeight() {
        return purchaseWeight;
    }

    public void setPurchaseWeight(Double purchaseWeight) {
        this.purchaseWeight = purchaseWeight;
    }

    public Float getPurchaseWeightRatio() {
        return purchaseWeightRatio;
    }

    public void setPurchaseWeightRatio(Float purchaseWeightRatio) {
        this.purchaseWeightRatio = purchaseWeightRatio;
    }

    public Long getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(Long purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public Float getPurchaseNumberRatio() {
        return purchaseNumberRatio;
    }

    public void setPurchaseNumberRatio(Float purchaseNumberRatio) {
        this.purchaseNumberRatio = purchaseNumberRatio;
    }

    public Boolean getInChina() {
        return InChina;
    }

    public void setInChina(Boolean inChina) {
        InChina = inChina;
    }

    public Long getPurchasesINChina() {
        return purchasesINChina;
    }

    public void setPurchasesINChina(Long purchasesINChina) {
        this.purchasesINChina = purchasesINChina;
    }

    public Float getPurchasesINChinaRatio() {
        return purchasesINChinaRatio;
    }

    public void setPurchasesINChinaRatio(Float purchasesINChinaRatio) {
        this.purchasesINChinaRatio = purchasesINChinaRatio;
    }

    public BigDecimal getPurchaseAmountINChinaUSD() {
        return PurchaseAmountINChinaUSD;
    }

    public void setPurchaseAmountINChinaUSD(BigDecimal purchaseAmountINChinaUSD) {
        PurchaseAmountINChinaUSD = purchaseAmountINChinaUSD;
    }

    public BigDecimal getPurchaseAmountINChinaUSDRatio() {
        return PurchaseAmountINChinaUSDRatio;
    }

    public void setPurchaseAmountINChinaUSDRatio(BigDecimal purchaseAmountINChinaUSDRatio) {
        PurchaseAmountINChinaUSDRatio = purchaseAmountINChinaUSDRatio;
    }

    public Double getPurchaseWeightINChina() {
        return PurchaseWeightINChina;
    }

    public void setPurchaseWeightINChina(Double purchaseWeightINChina) {
        PurchaseWeightINChina = purchaseWeightINChina;
    }

    public Double getPurchaseWeightINChinaRatio() {
        return PurchaseWeightINChinaRatio;
    }

    public void setPurchaseWeightINChinaRatio(Double purchaseWeightINChinaRatio) {
        PurchaseWeightINChinaRatio = purchaseWeightINChinaRatio;
    }

    public Long getPurchaseNumberINChina() {
        return purchaseNumberINChina;
    }

    public void setPurchaseNumberINChina(Long purchaseNumberINChina) {
        this.purchaseNumberINChina = purchaseNumberINChina;
    }

    public Long getPurchaseNumberINRatio() {
        return purchaseNumberINRatio;
    }

    public void setPurchaseNumberINRatio(Long purchaseNumberINRatio) {
        this.purchaseNumberINRatio = purchaseNumberINRatio;
    }

    public BigDecimal getTraderPrice() {
        return traderPrice;
    }

    public void setTraderPrice(BigDecimal traderPrice) {
        this.traderPrice = traderPrice;
    }

    public Long getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(Long totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }

    public Long getChinaSuppliers() {
        return chinaSuppliers;
    }

    public void setChinaSuppliers(Long chinaSuppliers) {
        this.chinaSuppliers = chinaSuppliers;
    }

    public Float getChinaRatio() {
        return chinaRatio;
    }

    public void setChinaRatio(Float chinaRatio) {
        this.chinaRatio = chinaRatio;
    }

    public Long getPeer() {
        return peer;
    }

    public void setPeer(Long peer) {
        this.peer = peer;
    }

    public Long getDomesticPeers() {
        return domesticPeers;
    }

    public void setDomesticPeers(Long domesticPeers) {
        this.domesticPeers = domesticPeers;
    }

    public Float getDomesticPeersRatio() {
        return domesticPeersRatio;
    }

    public void setDomesticPeersRatio(Float domesticPeersRatio) {
        this.domesticPeersRatio = domesticPeersRatio;
    }

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

    public BigDecimal getChinaProductVlUsdCif() {
        return chinaProductVlUsdCif;
    }

    public void setChinaProductVlUsdCif(BigDecimal chinaProductVlUsdCif) {
        this.chinaProductVlUsdCif = chinaProductVlUsdCif;
    }

    public BigDecimal getChinaProductVlUsd() {
        return chinaProductVlUsd;
    }

    public void setChinaProductVlUsd(BigDecimal chinaProductVlUsd) {
        this.chinaProductVlUsd = chinaProductVlUsd;
    }

    public BigDecimal getChinaProductVlUsdFob() {
        return chinaProductVlUsdFob;
    }

    public void setChinaProductVlUsdFob(BigDecimal chinaProductVlUsdFob) {
        this.chinaProductVlUsdFob = chinaProductVlUsdFob;
    }

    public BigDecimal getTraderPriceCif() {
        return traderPriceCif;
    }

    public void setTraderPriceCif(BigDecimal traderPriceCif) {
        this.traderPriceCif = traderPriceCif;
    }

    public BigDecimal getTraderPriceUsd() {
        return traderPriceUsd;
    }

    public void setTraderPriceUsd(BigDecimal traderPriceUsd) {
        this.traderPriceUsd = traderPriceUsd;
    }

    public BigDecimal getTraderPriceFob() {
        return traderPriceFob;
    }

    public void setTraderPriceFob(BigDecimal traderPriceFob) {
        this.traderPriceFob = traderPriceFob;
    }
}
