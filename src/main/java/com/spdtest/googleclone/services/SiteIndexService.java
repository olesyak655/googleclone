package com.spdtest.googleclone.services;

import com.spdtest.googleclone.GoogleCloneConstants;
import com.spdtest.googleclone.exceptions.AppException;
import com.spdtest.googleclone.models.SiteModel;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SiteIndexService implements GoogleCloneConstants {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SiteParseService siteParseService;
    private PropertyService propertyService;

    @Inject
    public SiteIndexService(SiteParseService siteParseService, PropertyService propertyService) {
        this.siteParseService = siteParseService;
        this.propertyService = propertyService;
    }

    public void indexSite(String url, int totalDepth) {
        Set<SiteModel> siteModels = siteParseService.generateSiteModels(url, totalDepth);

        writeIndexes(siteModels);
        logger.info("Indexes for site '{}' is built", url);
    }

    private void writeIndexes(Set<SiteModel> siteModels) {

        try (IndexWriter writer = createIndexWriter()) {

            List<Document> documents = new ArrayList<>();
            int i = 0;
            for (SiteModel siteModel : siteModels) {
                String content = siteParseService.generateText(siteModel.getUrl());
                Document document = createDocument(i, siteModel, content);
                i++;
                documents.add(document);
            }

            writer.deleteAll();
            writer.addDocuments(documents);
            writer.commit();

        } catch(IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Site cann't be indexed", e);
        }
    }

    private IndexWriter createIndexWriter() throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(propertyService.getIndexPath()));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        return new IndexWriter(directory, config);
    }

    private static Document createDocument(Integer id, SiteModel siteModel, String content) {
        Document document = new Document();
        document.add(new StringField(FIELD_ID, id.toString() , Field.Store.YES));
        document.add(new TextField(FIELD_URL, siteModel.getUrl() , Field.Store.YES));
        document.add(new TextField(FIELD_TITLE, siteModel.getTitle() , Field.Store.YES));
        document.add(new TextField(FIELD_CONTENT, content , Field.Store.YES));
        return document;
    }

    @VisibleForTesting
    SiteParseService getSiteParseService() {
        return siteParseService;
    }

    @VisibleForTesting
    void setSiteParseService(SiteParseService siteParseService) {
        this.siteParseService = siteParseService;
    }
}
