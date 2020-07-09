package com.meorient.mebuyerdiscovery.elasticsearch.doc;



import cn.hutool.core.date.DateUtil;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.util.StringUtils;

import java.util.Date;

import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.BILL_STANDARD_INDEX;
import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.BILL_STANDARD_INDEX_TYPE;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Document(indexName=BILL_STANDARD_INDEX,type =BILL_STANDARD_INDEX_TYPE )
public class LadingBillSimpleDocument {

    //    @Id
    private String id;

    private String theId;
    /**
     * '海关
     */
    @Field(searchAnalyzer="caseSensitive",analyzer = "english")
    private String hscode;
    /**
     * '进口商
     */
    private String importer;
    /**
     * '出口
     */
    private String exporter;
    /**
     * '产品单位vl美元cif
     */
    private String productUnitVlUsdCif;
    /**
     * '产品单位vl美元
     */
    private String productUnitVlUsd;
    /**
     * '产品单位vl美元fob
     */
    private String productUnitVlUsdFob;
    /**
     * '产品vl美元cif
     */
    private String productVlUsdCif;
    /**
     * '产品vl美元
     */
    private String productVlUsd;
    /**
     * '产品vl美元fob
     */
    private String productVlUsdFob;
    /**
     * '出口国
     */

    private String countryExport;
    /**
     * '出口国
     */
    private String countryOfExport;
    /**
     * '出口商id
     */
    private String exporterId;

    /**
     * '截止时间
     */
    private String byDate;
    /**
     * '国家
     */
    private String country;
    /**
     * '进口国
     */
    private String countryOfOrigin;
    /**
     * '入境口岸
     */
    @Field(searchAnalyzer="caseSensitive",analyzer = "english")
    private String custom;
    /**
     * '海关提交
     */
    private String customOfSubmission;
    /**
     * '送货定制
     */
    private String customOfDelivery;
    /**
     * '送货定制2
     */
    private String customOfDelivery2;
    /**
     * '出口报关
     */
    private String customOfExport;

    private String importerClean;

    private String exporterClean;
    /**
     * '提货单
     */
    private String customOfLading;
    /**
     * '收购国
     */
    private String countryOfAcquisition;
    /**
     * '提交日期
     */
//    private String dateOfSubmission;
    /**
     * '产品id
     */
    private String productId;

    /**
     * '进口商id
     */
    private String importerId;
    /**
     * '出口地址
     */
    private String exporterAddress;
    /**
     * '产品描述
     */
    @Field(searchAnalyzer="caseSensitive",analyzer = "english")
    private String productDescriptionInfo;
    /**
     * '商品价格
     */
    private double productVlUsdPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheId() {
        return theId;
    }

    public void setTheId(String theId) {
        this.theId = theId;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String getExporter() {
        return exporter;
    }

    public void setExporter(String exporter) {
        this.exporter = exporter;
    }

    public String getProductUnitVlUsdCif() {
        return productUnitVlUsdCif;
    }

    public void setProductUnitVlUsdCif(String productUnitVlUsdCif) {
        this.productUnitVlUsdCif = productUnitVlUsdCif;
    }

    public String getProductUnitVlUsd() {
        return productUnitVlUsd;
    }

    public void setProductUnitVlUsd(String productUnitVlUsd) {
        this.productUnitVlUsd = productUnitVlUsd;
    }

    public String getProductUnitVlUsdFob() {
        return productUnitVlUsdFob;
    }

    public void setProductUnitVlUsdFob(String productUnitVlUsdFob) {
        this.productUnitVlUsdFob = productUnitVlUsdFob;
    }

    public String getProductVlUsdCif() {
        return productVlUsdCif;
    }

    public void setProductVlUsdCif(String productVlUsdCif) {
        this.productVlUsdCif = productVlUsdCif;
    }

    public String getProductVlUsd() {
        return productVlUsd;
    }

    public void setProductVlUsd(String productVlUsd) {
        this.productVlUsd = productVlUsd;
    }

    public String getProductVlUsdFob() {
        return productVlUsdFob;
    }

    public void setProductVlUsdFob(String productVlUsdFob) {
        this.productVlUsdFob = productVlUsdFob;
    }

    public String getCountryExport() {
        return countryExport;
    }

    public void setCountryExport(String countryExport) {
        this.countryExport = countryExport;
    }

    public String getCountryOfExport() {
        return countryOfExport;
    }

    public void setCountryOfExport(String countryOfExport) {
        this.countryOfExport = countryOfExport;
    }

    public String getExporterId() {
        return exporterId;
    }

    public void setExporterId(String exporterId) {
        this.exporterId = exporterId;
    }

    public String getByDate() {
        return byDate;
    }

    public void setByDate(String byDate) {
        this.byDate = byDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getCustomOfSubmission() {
        return customOfSubmission;
    }

    public void setCustomOfSubmission(String customOfSubmission) {
        this.customOfSubmission = customOfSubmission;
    }

    public String getCustomOfDelivery() {
        return customOfDelivery;
    }

    public void setCustomOfDelivery(String customOfDelivery) {
        this.customOfDelivery = customOfDelivery;
    }

    public String getCustomOfDelivery2() {
        return customOfDelivery2;
    }

    public void setCustomOfDelivery2(String customOfDelivery2) {
        this.customOfDelivery2 = customOfDelivery2;
    }

    public String getCustomOfExport() {
        return customOfExport;
    }

    public void setCustomOfExport(String customOfExport) {
        this.customOfExport = customOfExport;
    }

    public String getImporterClean() {
        return importerClean;
    }

    public void setImporterClean(String importerClean) {
        this.importerClean = importerClean;
    }

    public String getExporterClean() {
        return exporterClean;
    }

    public void setExporterClean(String exporterClean) {
        this.exporterClean = exporterClean;
    }

    public String getCustomOfLading() {
        return customOfLading;
    }

    public void setCustomOfLading(String customOfLading) {
        this.customOfLading = customOfLading;
    }

    public String getCountryOfAcquisition() {
        return countryOfAcquisition;
    }

    public void setCountryOfAcquisition(String countryOfAcquisition) {
        this.countryOfAcquisition = countryOfAcquisition;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImporterId() {
        return importerId;
    }

    public void setImporterId(String importerId) {
        this.importerId = importerId;
    }

    public String getExporterAddress() {
        return exporterAddress;
    }

    public void setExporterAddress(String exporterAddress) {
        this.exporterAddress = exporterAddress;
    }

    public String getProductDescriptionInfo() {
        return productDescriptionInfo;
    }

    public void setProductDescriptionInfo(String productDescriptionInfo) {
        this.productDescriptionInfo = productDescriptionInfo;
    }

    public double getProductVlUsdPrice() {
        return productVlUsdPrice;
    }

    public void setProductVlUsdPrice(double productVlUsdPrice) {
        this.productVlUsdPrice = productVlUsdPrice;
    }
}
