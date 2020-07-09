package com.meorient.mebuyerdiscovery.elasticsearch.doc;



import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.ALL_COUNTRY_BALLING_INDEX;
import static com.meorient.mebuyerdiscovery.elasticsearch.constant.ElasticSearchConstant.ALL_COUNTRY_BALLING_TYPE;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Document(indexName=ALL_COUNTRY_BALLING_INDEX,type =ALL_COUNTRY_BALLING_TYPE )
public class OneRecordDocument {

    /**
     * 'THE_ID
     */
//    @Id
    @Field
    private String theId;
    /**
     * '截止时间
     */
    @Field
    private String byDate;
    /**
     * '支付方式
     */
    @Field
    private String paymentWay;
    /**
     * '加载状态
     */
    @Field
    private String loadStatus;
    /**
     * '品牌模型
     */
    @Field
    private String brandModel;
    /**
     * '国家
     */
    @Field
    private String country;
    /**
     * '执照商
     */
    @Field
    private String manufacturer;
    /**
     * '收购国
     */
    @Field
    private String countryOfAcquisition;
    /**
     * '购买国
     */
    @Field
    private String countryOfPurchase;
    /**
     * '出口国
     */
    @Field
    private String countryOfExport;
    @Field
    private String countryExport;
    /**
     * '进口国
     */
    @Field
    private String countryOfOrigin;
    /**
     * '入境口岸
     */
    @Field
    private String custom;
    /**
     * '报关代理
     */
    @Field
    private String customsAgent;
    /**
     * '海关名称
     */
    @Field
    private String customsProcedureName;
    /**
     * '海关提交
     */
    @Field
    private String customOfSubmission;
    /**
     * '送货定制
     */
    @Field
    private String customOfDelivery;
    /**
     * '送货定制2
     */
    @Field
    private String customOfDelivery2;
    /**
     * '出口报关
     */
    @Field
    private String customOfExport;
    /**
     * '提货单
     */
    @Field
    private String customOfLading;
    /**
     * '货柜代码
     */
    @Field
    private String containerCode;
    /**
     * '提交日期
     */
    @Field
    private String dateOfSubmission;
    /**
     * '货物净重
     */
    @Field
    private String grossWeightKg;
    /**
     * '货物喊价
     */
    @Field
    private String cargoCry;
    /**
     * '货物日期
     */
    @Field
    private String cargoDate;
    /**
     * '货物喊价
     */
    @Field
    private String cargoPriceOtCry;
    /**
     * '货物价格usd
     */
    @Field
    private String cargoPriceUsd;
    /**
     * '货物类型名称
     */
    @Field
    private String cargoTypeName;
    /**
     * '海关
     */
    @Field(type = Keyword)
    private String hscode;
    /**
     * '海关章
     */
    @Field
    private String hscodeChapter;
    /**
     * '净重
     */
    @Field
    private String netWeightKg;
    /**
     * '到岸价
     */
    @Field
    private String cifCry;
    /**
     * '产品单位数量
     */
    @Field
    private String unitOfProductQuantity;
    /**
     * '单位产品数量2
     */
//    UNIT_OF_PRODUCT_QUANTITY2;
    /**
     * '提货单
     */
    @Field
    private String billOfLading;
    /**
     * 'house单号
     */
    @Field
    private String billOfLadingHouse;
    /**
     * 'master单号
     */
    @Field
    private String billOfLadingMaster;
    /**
     * '提货日期
     */
    @Field
    private String ladingDate;
    /**
     * '运送商
     */
    @Field
    private String carrierAgent;
    /**
     * '出口
     */
    @Field
    private String exporter;
    /**
     * '出口地址
     */
    @Field
    private String exporterAddress;
    /**
     * '出口城市
     */
    @Field
    private String exporterCity;
    /**
     * '出口商id
     */
    @Field
    private String exporterId;
    /**
     * '出口商email
     */
    @Field
    private String exporterEmail;
    /**
     * '新字段
     */
//    NEW_FIELD;
    @Field
    private String newField1;
    /**
     * '产品id
     */
    @Field
    private String productId;
    /**
     * '产品描述
     */
    @Field(index=false)
    private String productDescription;
    @Field(index=false)
    private String productDescription2;
    @Field(index=false)
    private String productDescription3;
    /**
     * '产品数量
     */
    @Field
    private String productQuantity;
    /**
     * '产品数量2
     */
    @Field
    private String productQuantity2;
    /**
     * '产品喊价
     */
    @Field
    private String productVlOtCry;
    /**
     * '产品VlOtCryCif
     */
    @Field
    private String productVlOtCryCif;
    @Field
    private String productVlOtCryFob;
    /**
     * '产品UnitVlOtCry
     */
    @Field
    private String productUnitVlOtCry;
    @Field
    private String productUnitVlUsd;
    @Field
    private String productUnitVlUsdCif;
    /**
     * '产品单位vl美元fob
     */
    @Field
    private String productUnitVlUsdFob;
    /**
     * '产品单位vl美元cif
     */
    @Field
    private String productUnitVlOtCryCif;
    /**
     * '产品单位vl美元fob
     */
    @Field
    private String productUnitVlOtCryFob;


    @Field(type = FieldType.Double)
    private String productVlUsd;
    /**
     * '产品VlOtCryCif
     */
    @Field(type = FieldType.Double)
    private String productVlUsdCif;

    @Field(type = FieldType.Double)
    private String productVlUsdFob;
    /**
     * '真实名称
     */
    @Field
    private String rawName;
    /**
     * '国际贸易术语
     */
    @Field
    private String incoterms;
    /**
     * '进口网站
     */
    @Field
    private String importerWebsite;
    /**
     * '导入RucRut
     */
    @Field
    private String importerRucRut;
    /**
     * '进口商
     */
    @Field
    private String importer;
    /**
     * '进口城市
     */
    @Field
    private String importerCity;
    /**
     * '进口编号
     */
    @Field
    private String importerCode;
    /**
     * '进口状态
     */
    @Field
    private String importerState;
    /**
     * '进口商id
     */
    @Field
    private String importerId;
    /**
     * '入站输入类型
     */
    @Field
    private String inboundEntryType;
    /**
     * '保险喊价
     */
    @Field
    private String insuranceOtCry;
    /**
     * '保险美元
     */
    @Field
    private String insuranceUsd;
    /**
     * '保险索赔
     */
    @Field
    private String insuranceCry;
    /**
     * '总税额usd
     */
    @Field
    private String totalTaxUsd;
    /**
     * '到达日期
     */
    @Field
    private String arrivalDate;
    /**
     * '到达日期2
     */
    @Field
    private String arrivalDate2;
    /**
     * '声明id
     */
    @Field
    private String declarationId;
    /**
     * '声明Id2
     */
    @Field
    private String declarationId2;
    /**
     * '自由区
     */
    @Field
    private String freeZone;
    /**
     * '正在发货
     */
    @Field
    private String isShipping;
    /**
     * 'frob指示
     */
    @Field
    private String frobIndicator;
    /**
     * '程序国家
     */
    @Field
    private String procedenceCountry;
    /**
     * '联系人
     */
    @Field
    private String contactPerson;
    /**
     * '邮箱
     */
    @Field
    private String email;
    /**
     * '传真
     */
    @Field
    private String fax;
    /**
     * '船编号
     */
    @Field
    private String vesselCode;
    /**
     * '船名称
     */
    @Field
    private String vesselName;
    /**
     * '船的标志
     */
    @Field
    private String vesselFlag;
    /**
     * '旅游代码
     */
    @Field
    private String voyageCode;
    /**
     * '电话
     */
    @Field
    private String phone;
    @Field
    private String lid;
    @Field
    private String importerClean;
    @Field
    private String exporterClean;
    @Field
    private String unitOfProductQuantity2;

    @Field(searchAnalyzer="caseSensitive",analyzer = "english")
    private String productDescriptionInfo;


    public String getProductDescriptionInfo() {
        return productDescriptionInfo;
    }

    public void setProductDescriptionInfo(String productDescriptionInfo) {
        this.productDescriptionInfo = productDescriptionInfo;
    }

    public String getTheId() {
        return theId;
    }

    public void setTheId(String theId) {
        this.theId = theId;
    }

    public String getByDate() {
        return byDate;
    }

    public void setByDate(String byDate) {
        this.byDate = byDate;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCountryOfAcquisition() {
        return countryOfAcquisition;
    }

    public void setCountryOfAcquisition(String countryOfAcquisition) {
        this.countryOfAcquisition = countryOfAcquisition;
    }

    public String getCountryOfPurchase() {
        return countryOfPurchase;
    }

    public void setCountryOfPurchase(String countryOfPurchase) {
        this.countryOfPurchase = countryOfPurchase;
    }

    public String getCountryOfExport() {
        return countryOfExport;
    }

    public void setCountryOfExport(String countryOfExport) {
        this.countryOfExport = countryOfExport;
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

    public String getCustomsAgent() {
        return customsAgent;
    }

    public void setCustomsAgent(String customsAgent) {
        this.customsAgent = customsAgent;
    }

    public String getCustomsProcedureName() {
        return customsProcedureName;
    }

    public void setCustomsProcedureName(String customsProcedureName) {
        this.customsProcedureName = customsProcedureName;
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

    public String getCustomOfLading() {
        return customOfLading;
    }

    public void setCustomOfLading(String customOfLading) {
        this.customOfLading = customOfLading;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(String dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public String getGrossWeightKg() {
        return grossWeightKg;
    }

    public void setGrossWeightKg(String grossWeightKg) {
        this.grossWeightKg = grossWeightKg;
    }

    public String getCargoCry() {
        return cargoCry;
    }

    public void setCargoCry(String cargoCry) {
        this.cargoCry = cargoCry;
    }

    public String getCargoDate() {
        return cargoDate;
    }

    public void setCargoDate(String cargoDate) {
        this.cargoDate = cargoDate;
    }

    public String getCargoPriceOtCry() {
        return cargoPriceOtCry;
    }

    public void setCargoPriceOtCry(String cargoPriceOtCry) {
        this.cargoPriceOtCry = cargoPriceOtCry;
    }

    public String getCargoPriceUsd() {
        return cargoPriceUsd;
    }

    public void setCargoPriceUsd(String cargoPriceUsd) {
        this.cargoPriceUsd = cargoPriceUsd;
    }

    public String getCargoTypeName() {
        return cargoTypeName;
    }

    public void setCargoTypeName(String cargoTypeName) {
        this.cargoTypeName = cargoTypeName;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getHscodeChapter() {
        return hscodeChapter;
    }

    public void setHscodeChapter(String hscodeChapter) {
        this.hscodeChapter = hscodeChapter;
    }

    public String getNetWeightKg() {
        return netWeightKg;
    }

    public void setNetWeightKg(String netWeightKg) {
        this.netWeightKg = netWeightKg;
    }

    public String getCifCry() {
        return cifCry;
    }

    public void setCifCry(String cifCry) {
        this.cifCry = cifCry;
    }

    public String getUnitOfProductQuantity() {
        return unitOfProductQuantity;
    }

    public void setUnitOfProductQuantity(String unitOfProductQuantity) {
        this.unitOfProductQuantity = unitOfProductQuantity;
    }

    public String getBillOfLading() {
        return billOfLading;
    }

    public void setBillOfLading(String billOfLading) {
        this.billOfLading = billOfLading;
    }

    public String getBillOfLadingHouse() {
        return billOfLadingHouse;
    }

    public void setBillOfLadingHouse(String billOfLadingHouse) {
        this.billOfLadingHouse = billOfLadingHouse;
    }

    public String getBillOfLadingMaster() {
        return billOfLadingMaster;
    }

    public void setBillOfLadingMaster(String billOfLadingMaster) {
        this.billOfLadingMaster = billOfLadingMaster;
    }

    public String getLadingDate() {
        return ladingDate;
    }

    public void setLadingDate(String ladingDate) {
        this.ladingDate = ladingDate;
    }

    public String getCarrierAgent() {
        return carrierAgent;
    }

    public void setCarrierAgent(String carrierAgent) {
        this.carrierAgent = carrierAgent;
    }

    public String getExporter() {
        return exporter;
    }

    public void setExporter(String exporter) {
        this.exporter = exporter;
    }

    public String getExporterAddress() {
        return exporterAddress;
    }

    public void setExporterAddress(String exporterAddress) {
        this.exporterAddress = exporterAddress;
    }

    public String getExporterCity() {
        return exporterCity;
    }

    public void setExporterCity(String exporterCity) {
        this.exporterCity = exporterCity;
    }

    public String getExporterId() {
        return exporterId;
    }

    public void setExporterId(String exporterId) {
        this.exporterId = exporterId;
    }

    public String getExporterEmail() {
        return exporterEmail;
    }

    public void setExporterEmail(String exporterEmail) {
        this.exporterEmail = exporterEmail;
    }

    public String getNewField1() {
        return newField1;
    }

    public void setNewField1(String newField1) {
        this.newField1 = newField1;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductDescription2() {
        return productDescription2;
    }

    public void setProductDescription2(String productDescription2) {
        this.productDescription2 = productDescription2;
    }

    public String getProductDescription3() {
        return productDescription3;
    }

    public void setProductDescription3(String productDescription3) {
        this.productDescription3 = productDescription3;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductQuantity2() {
        return productQuantity2;
    }

    public void setProductQuantity2(String productQuantity2) {
        this.productQuantity2 = productQuantity2;
    }

    public String getProductVlOtCry() {
        return productVlOtCry;
    }

    public void setProductVlOtCry(String productVlOtCry) {
        this.productVlOtCry = productVlOtCry;
    }

    public String getProductVlOtCryCif() {
        return productVlOtCryCif;
    }

    public void setProductVlOtCryCif(String productVlOtCryCif) {
        this.productVlOtCryCif = productVlOtCryCif;
    }

    public String getProductVlOtCryFob() {
        return productVlOtCryFob;
    }

    public void setProductVlOtCryFob(String productVlOtCryFob) {
        this.productVlOtCryFob = productVlOtCryFob;
    }

    public String getProductUnitVlOtCry() {
        return productUnitVlOtCry;
    }

    public void setProductUnitVlOtCry(String productUnitVlOtCry) {
        this.productUnitVlOtCry = productUnitVlOtCry;
    }

    public String getProductUnitVlUsd() {
        return productUnitVlUsd;
    }

    public void setProductUnitVlUsd(String productUnitVlUsd) {
        this.productUnitVlUsd = productUnitVlUsd;
    }

    public String getProductUnitVlUsdCif() {
        return productUnitVlUsdCif;
    }

    public void setProductUnitVlUsdCif(String productUnitVlUsdCif) {
        this.productUnitVlUsdCif = productUnitVlUsdCif;
    }

    public String getProductUnitVlUsdFob() {
        return productUnitVlUsdFob;
    }

    public void setProductUnitVlUsdFob(String productUnitVlUsdFob) {
        this.productUnitVlUsdFob = productUnitVlUsdFob;
    }

    public String getProductUnitVlOtCryCif() {
        return productUnitVlOtCryCif;
    }

    public void setProductUnitVlOtCryCif(String productUnitVlOtCryCif) {
        this.productUnitVlOtCryCif = productUnitVlOtCryCif;
    }

    public String getProductUnitVlOtCryFob() {
        return productUnitVlOtCryFob;
    }

    public void setProductUnitVlOtCryFob(String productUnitVlOtCryFob) {
        this.productUnitVlOtCryFob = productUnitVlOtCryFob;
    }

    public String getProductVlUsd() {
        return productVlUsd;
    }

    public void setProductVlUsd(String productVlUsd) {
        this.productVlUsd = productVlUsd;
    }

    public String getProductVlUsdCif() {
        return productVlUsdCif;
    }

    public void setProductVlUsdCif(String productVlUsdCif) {
        this.productVlUsdCif = productVlUsdCif;
    }

    public String getProductVlUsdFob() {
        return productVlUsdFob;
    }

    public void setProductVlUsdFob(String productVlUsdFob) {
        this.productVlUsdFob = productVlUsdFob;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String getIncoterms() {
        return incoterms;
    }

    public void setIncoterms(String incoterms) {
        this.incoterms = incoterms;
    }

    public String getImporterWebsite() {
        return importerWebsite;
    }

    public void setImporterWebsite(String importerWebsite) {
        this.importerWebsite = importerWebsite;
    }

    public String getImporterRucRut() {
        return importerRucRut;
    }

    public void setImporterRucRut(String importerRucRut) {
        this.importerRucRut = importerRucRut;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String getImporterCity() {
        return importerCity;
    }

    public void setImporterCity(String importerCity) {
        this.importerCity = importerCity;
    }

    public String getImporterCode() {
        return importerCode;
    }

    public void setImporterCode(String importerCode) {
        this.importerCode = importerCode;
    }

    public String getImporterState() {
        return importerState;
    }

    public void setImporterState(String importerState) {
        this.importerState = importerState;
    }

    public String getImporterId() {
        return importerId;
    }

    public void setImporterId(String importerId) {
        this.importerId = importerId;
    }

    public String getInboundEntryType() {
        return inboundEntryType;
    }

    public void setInboundEntryType(String inboundEntryType) {
        this.inboundEntryType = inboundEntryType;
    }

    public String getInsuranceOtCry() {
        return insuranceOtCry;
    }

    public void setInsuranceOtCry(String insuranceOtCry) {
        this.insuranceOtCry = insuranceOtCry;
    }

    public String getInsuranceUsd() {
        return insuranceUsd;
    }

    public void setInsuranceUsd(String insuranceUsd) {
        this.insuranceUsd = insuranceUsd;
    }

    public String getInsuranceCry() {
        return insuranceCry;
    }

    public void setInsuranceCry(String insuranceCry) {
        this.insuranceCry = insuranceCry;
    }

    public String getTotalTaxUsd() {
        return totalTaxUsd;
    }

    public void setTotalTaxUsd(String totalTaxUsd) {
        this.totalTaxUsd = totalTaxUsd;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalDate2() {
        return arrivalDate2;
    }

    public void setArrivalDate2(String arrivalDate2) {
        this.arrivalDate2 = arrivalDate2;
    }

    public String getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(String declarationId) {
        this.declarationId = declarationId;
    }

    public String getDeclarationId2() {
        return declarationId2;
    }

    public void setDeclarationId2(String declarationId2) {
        this.declarationId2 = declarationId2;
    }

    public String getFreeZone() {
        return freeZone;
    }

    public void setFreeZone(String freeZone) {
        this.freeZone = freeZone;
    }

    public String getIsShipping() {
        return isShipping;
    }

    public void setIsShipping(String isShipping) {
        this.isShipping = isShipping;
    }

    public String getFrobIndicator() {
        return frobIndicator;
    }

    public void setFrobIndicator(String frobIndicator) {
        this.frobIndicator = frobIndicator;
    }

    public String getProcedenceCountry() {
        return procedenceCountry;
    }

    public void setProcedenceCountry(String procedenceCountry) {
        this.procedenceCountry = procedenceCountry;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getVesselCode() {
        return vesselCode;
    }

    public void setVesselCode(String vesselCode) {
        this.vesselCode = vesselCode;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getVesselFlag() {
        return vesselFlag;
    }

    public void setVesselFlag(String vesselFlag) {
        this.vesselFlag = vesselFlag;
    }

    public String getVoyageCode() {
        return voyageCode;
    }

    public void setVoyageCode(String voyageCode) {
        this.voyageCode = voyageCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
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

    public String getUnitOfProductQuantity2() {
        return unitOfProductQuantity2;
    }

    public void setUnitOfProductQuantity2(String unitOfProductQuantity2) {
        this.unitOfProductQuantity2 = unitOfProductQuantity2;
    }



    public String getCountryExport() {
        return countryExport;
    }

    public void setCountryExport(String countryExport) {
        this.countryExport = countryExport;
    }
}
