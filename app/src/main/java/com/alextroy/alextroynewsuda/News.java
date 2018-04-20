package com.alextroy.alextroynewsuda;

public class News {

    private String webTitle;
    private String webPublicationDate;
    private String webSectionName;
    private String webUrl;
    private String webAuthor;

    public News(String webTitle, String webPublicationDate, String webSectionName, String webUrl, String webAuthor) {
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webSectionName = webSectionName;
        this.webUrl = webUrl;
        this.webAuthor = webAuthor;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebSectionName() {
        return webSectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getWebAuthor() {
        return webAuthor;
    }
}
