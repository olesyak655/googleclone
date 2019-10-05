package com.spdtest.googleclone.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PropertyService {

    @Inject
    private Environment environment;

    @Value("${googleclone.index-path}")
    private String indexPath;

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }
}
