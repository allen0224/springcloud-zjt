package com.meorient.mebuyerdiscovery.elasticsearch.doc;

import org.springframework.data.elasticsearch.annotations.Document;

import static com.meorient.mebuyerdiscovery.elasticsearch.doc.Statistics.COUNTRY_INDEX;
import static com.meorient.mebuyerdiscovery.elasticsearch.doc.Statistics.COUNTRY_TYPE;


@Document(indexName = COUNTRY_INDEX,type =COUNTRY_TYPE)
public class CountryStatistics {

    public static final String buyersCount="buyers_count";

    public static final String suppliersCount="suppliers_count";

    public static final  String code_key="code_key";

    public static final String country_name="country_name";

    //国家
    private String country;
    // 编码
    private String code;
    //采购商数
    private Long buyers;
    //供应商数
    private Long suppliers;

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

    public Long getBuyers() {
        return buyers;
    }

    public void setBuyers(Long buyers) {
        this.buyers = buyers;
    }

    public Long getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Long suppliers) {
        this.suppliers = suppliers;
    }
}
