package com.meorient.mebuyerdiscovery.elasticsearch.doc;

import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

import static com.meorient.mebuyerdiscovery.elasticsearch.doc.Statistics.COMPANY_INDEX;
import static com.meorient.mebuyerdiscovery.elasticsearch.doc.Statistics.COMPANY_TYPE;


@Document(indexName = COMPANY_INDEX,type = COMPANY_TYPE)
public class CompanyStatistics {

    public static final String PRODUCT_VL_USD_CIF_SUM="productVlUsdCifSum";
    public static final String PRODUCT_VL_USD_SUM="productVlUsdSum";
    public static final String PRODUCT_VL_USD_FOB_SUM="productVlUsdFobSum";


    private BigDecimal productVlUsdCif;

    private BigDecimal productVlUsd;

    private BigDecimal productVlUsdFob;


    private BigDecimal chinaProductVlUsdCif;

    private BigDecimal chinaProductVlUsd;


    private BigDecimal chinaProductVlUsdFob;

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

    //公司ID
    private String companyId;
    //公司名称
    private String companyName;
    //公司中文名称
    private String companyName_en;
    //国家
    private String country;
    //-区域
    private String are;
    //-州/省
    private String province_state;
    //-公司性质
    private String companyType;
    //总采购次数
    private Long totalPurchases;
    //总采购金额(USD)
    private BigDecimal totalPurchaseAmountUSD;
    //-总采购重量
    private Double totalPurchaseWeight;
    //-总采购件数
    private Long totalPurchasesNumber;
    //是否从中国进口
    private Boolean InChina;
    //从中国的采购次数
    private Long purchasesINChina;
    //从中国的采购金额(usd)
    private BigDecimal totalPurchaseAmountINChinaUSD;
    //-从中国的采购重量(KG)
    private Double PurchaseWeightINChina;
    //从中国的采购件数
    private Long purchaseNumberINChina;
    //总供应商数
    private Long totalSuppliers;
    //中国供应商数
    private Long chinaSuppliers;
    //中国供应商占比（该公司中国供应商数/该公司总供应商数）*100%
    private Long chinaRatio;
    //同行数（指采购相同编码的全球采购商）
    private Long peer;
    //本国同行数（指采购相同编码的本国采购商）
    private Long domesticPeers;
    //本国同行占比（采购相同编码的本国同行/全球同行数）*100%
    private Float domesticPeersRatio;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName_en() {
        return companyName_en;
    }

    public void setCompanyName_en(String companyName_en) {
        this.companyName_en = companyName_en;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAre() {
        return are;
    }

    public void setAre(String are) {
        this.are = are;
    }

    public String getProvince_state() {
        return province_state;
    }

    public void setProvince_state(String province_state) {
        this.province_state = province_state;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public Long getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(Long totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public BigDecimal getTotalPurchaseAmountUSD() {
        return totalPurchaseAmountUSD;
    }

    public void setTotalPurchaseAmountUSD(BigDecimal totalPurchaseAmountUSD) {
        this.totalPurchaseAmountUSD = totalPurchaseAmountUSD;
    }

    public Double getTotalPurchaseWeight() {
        return totalPurchaseWeight;
    }

    public void setTotalPurchaseWeight(Double totalPurchaseWeight) {
        this.totalPurchaseWeight = totalPurchaseWeight;
    }

    public Long getTotalPurchasesNumber() {
        return totalPurchasesNumber;
    }

    public void setTotalPurchasesNumber(Long totalPurchasesNumber) {
        this.totalPurchasesNumber = totalPurchasesNumber;
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

    public BigDecimal getTotalPurchaseAmountINChinaUSD() {
        return totalPurchaseAmountINChinaUSD;
    }

    public void setTotalPurchaseAmountINChinaUSD(BigDecimal totalPurchaseAmountINChinaUSD) {
        this.totalPurchaseAmountINChinaUSD = totalPurchaseAmountINChinaUSD;
    }

    public Double getPurchaseWeightINChina() {
        return PurchaseWeightINChina;
    }

    public void setPurchaseWeightINChina(Double purchaseWeightINChina) {
        PurchaseWeightINChina = purchaseWeightINChina;
    }

    public Long getPurchaseNumberINChina() {
        return purchaseNumberINChina;
    }

    public void setPurchaseNumberINChina(Long purchaseNumberINChina) {
        this.purchaseNumberINChina = purchaseNumberINChina;
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

    public Long getChinaRatio() {
        return chinaRatio;
    }

    public void setChinaRatio(Long chinaRatio) {
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
}
