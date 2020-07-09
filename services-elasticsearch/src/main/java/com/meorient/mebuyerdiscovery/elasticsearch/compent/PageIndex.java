package com.meorient.mebuyerdiscovery.elasticsearch.compent;



import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class PageIndex<T> {

    public final static PageIndex end= new PageIndex();

    public String startIndex;

    public String endIndex;

    public List<T> list;

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(list);
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageIndex{" +
                "startIndex='" + startIndex + '\'' +
                ", endIndex='" + endIndex + '\'' +
                '}';
    }
}
