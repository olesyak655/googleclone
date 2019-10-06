package com.spdtest.googleclone.services;

import com.spdtest.googleclone.GoogleCloneConstants;
import com.spdtest.googleclone.exceptions.AppException;
import com.spdtest.googleclone.models.SiteModel;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
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

@Service
public class SiteSearchService implements GoogleCloneConstants {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyService propertyService;

    public List<SiteModel> search(String textToFind) {
        try {
            IndexSearcher searcher = createIndexSearcher();

            TopDocs foundDocs = searchInContent(textToFind, searcher);
            List<SiteModel> searchedSiteModels = new ArrayList<>();

            logger.info("Total Results : {} ", foundDocs.totalHits);

            for (ScoreDoc scoreDoc : foundDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);

                logger.info("URL : {}, Title : {}, Score : {}", document.get(FIELD_URL), document.get(FIELD_TITLE), scoreDoc.score);

                SiteModel siteModel = new SiteModel();
                siteModel.setUrl(document.get(FIELD_URL));
                siteModel.setTitle(document.get(FIELD_TITLE));

                searchedSiteModels.add(siteModel);
            }

            return searchedSiteModels;
        } catch (Exception e) {
            throw new AppException(HttpStatus.NOT_FOUND, "Search by text " + textToFind + " is failed", e);
        }
    }

    private IndexSearcher createIndexSearcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(propertyService.getIndexPath()));
        IndexReader reader = DirectoryReader.open(dir);
        return new IndexSearcher(reader);
    }

    private TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception {
        QueryParser qp = new QueryParser(FIELD_CONTENT, new StandardAnalyzer());
        Query query = qp.parse(textToFind);

        return searcher.search(query, 10);
    }

    @VisibleForTesting
    PropertyService getPropertyService() {
        return propertyService;
    }

    @VisibleForTesting
    void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
}
