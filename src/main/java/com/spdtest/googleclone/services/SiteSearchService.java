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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class SiteSearchService implements GoogleCloneConstants {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<SiteModel> search(String textToFind) {
        try {
            IndexSearcher searcher = createIndexSearcher();

            TopDocs foundDocs = searchInContent(textToFind, searcher);
            List<SiteModel> searchedSiteModels = new ArrayList<>();

            logger.info("Total Results : {} " + foundDocs.totalHits);
            System.out.println("Total Results :: " + foundDocs.totalHits);

            for (ScoreDoc scoreDoc : foundDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                System.out.println("URL : " + document.get(FIELD_URL) + "\nTitle : " + document.get(FIELD_TITLE) + ", \nScore : " + scoreDoc.score);

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

    private static IndexSearcher createIndexSearcher() throws IOException
    {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        return new IndexSearcher(reader);
    }

    private static TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser(FIELD_CONTENT, new StandardAnalyzer());
        Query query = qp.parse(textToFind);

        return searcher.search(query, 10);
    }
}
