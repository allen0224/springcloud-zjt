package com.meorient.mebuyerdiscovery.elasticsearch.compent;

import org.apache.commons.collections.functors.FalsePredicate;

import java.awt.image.VolatileImage;

public class PageBlock {

    public volatile boolean isEnd= false;
    public Integer page;
    public Integer step;

    public Integer size;

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageBlock{" +
                "page='" + page + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
