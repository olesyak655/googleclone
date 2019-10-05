package com.spdtest.googleclone.models;

public class SiteModel {

    private String url;
    private String title;

    public SiteModel() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteModel siteModel = (SiteModel) o;

        if (!url.equals(siteModel.url)) return false;
        return title != null ? title.equals(siteModel.title) : siteModel.title == null;
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
