package com.meorient.mebuyerdiscovery.elasticsearch.mongdb.service;


import com.meorient.mebuyerdiscovery.elasticsearch.mongdb.data.Company;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class OneRecordRecordMongo {

    private final MongoTemplate mongoTemplate;

    private final String collectionCompany = "company_name";

    public OneRecordRecordMongo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Company> companyPage(int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit);
        List<HashMap> company_nameList = mongoTemplate.find(query, HashMap.class, collectionCompany);
        List<Company> collect = company_nameList.stream().map(this::build).collect(Collectors.toList());
        return collect;
    }


    public Company build(Map keyValue) {
        String country = (String) keyValue.get("COUNTRY");
        String company_name = (String) keyValue.get("COMPANY_NAME");
        String best_clean = (String) keyValue.get("BEST_CLEAN");
        String _id = (String) keyValue.get("_id");
        Company company = new Company();
        company.setId(_id);
        company.setBestClean(best_clean);
        company.setCompanyName(company_name);
        company.setCountry(country);
        return company;
    }
}
