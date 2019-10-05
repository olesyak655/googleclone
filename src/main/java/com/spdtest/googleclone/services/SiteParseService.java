package com.spdtest.googleclone.services;

import com.spdtest.googleclone.models.SiteModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Service
public class SiteParseService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Set<SiteModel> generateSiteModels(String url, int totalDepth) {

        Set<SiteModel> siteModels = new HashSet<>();
        generateSiteModels(url, siteModels, 0, totalDepth);

        logger.info("Set of the site models with url and title is generated. Size: {}", siteModels.size());

        return siteModels;
    }

    private void generateSiteModels(String url, Set<SiteModel> siteModels, int currentDepth, int totalDepth) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements documentLinks = document.select("a");

            String title = generateTitle(document);

            SiteModel siteModel = new SiteModel();
            siteModel.setUrl(url);
            siteModel.setTitle(title);

            currentDepth++;

            boolean isAddToSet = siteModels.add(siteModel);

            if (documentLinks.isEmpty() || currentDepth > totalDepth || !isAddToSet) {
                return;
            }
            final int currentDepthFinal = currentDepth;
            documentLinks.stream()
                    .map(documentLink -> documentLink.absUrl("href")) //"abs:href"
                    .forEachOrdered(documentUrl -> {
                        System.out.println(documentUrl);
                        generateSiteModels(documentUrl, siteModels, currentDepthFinal, totalDepth);
                    });

        } catch (Exception e) {
            logger.error("Parsing the site by the url '{}' is failed. This url will be skiped in the indexing. {}", url, e.getMessage());
            return;
        }
    }

    public String generateText(String url) {

        try {
            return Jsoup.parse(new URL(url), 100000).text();
        } catch (IOException e) {
            logger.error("Parsing the site text by the url '{}' is failed. {}", url, e.getMessage());
            return "";
        }
    }

    private String generateTitle(Document document) {
        Elements titles = document.select("title");
        return !titles.isEmpty() ? titles.first().text() : "";
    }
}
