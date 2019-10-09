package com.spdtest.googleclone.services;

import com.spdtest.googleclone.models.SiteModel;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class SiteIndexServiceTest {

    private static final String URL_1 = "https://mysite.io/guides/";
    private static final String URL_2 = "https://mysite.io/";

    private static final String TITLE_1 = "MySite guide";
    private static final String TITLE_2 = "MySite";

    private SiteIndexService siteIndexService;
    private PropertyService propertyService;

    private SiteParseService mockedSiteParseService;

    @Before
    public void setUp() {

        this.mockedSiteParseService = mock(SiteParseService.class);

        this.propertyService = new PropertyService();
        propertyService.setIndexPath("./temp_test/google_clone_index");

        this.siteIndexService = new SiteIndexService(mockedSiteParseService, propertyService);
    }

    @Test
    public void testIndexSite() throws IOException {
        File directory = new File(propertyService.getIndexPath());
        if (directory.exists()) {
            FileUtils.deleteDirectory(directory);
        }

        SiteModel siteModel1 = createSiteModel(URL_1, TITLE_1);
        SiteModel siteModel2 = createSiteModel(URL_2, TITLE_2);
        Set<SiteModel> siteModels = new HashSet<>();
        siteModels.add(siteModel1);
        siteModels.add(siteModel2);

        when(mockedSiteParseService.generateSiteModels(URL_1, 2)).thenReturn(siteModels);

        doReturn("Content from site mysite.io").doReturn("Content from site mysite.io/guides with guides")
                .when(mockedSiteParseService).generateText(any());

        siteIndexService.indexSite(URL_1, 2);

        assertTrue(new File(propertyService.getIndexPath()).exists());
    }

    private SiteModel createSiteModel(String url, String title) {

        SiteModel siteModel = new SiteModel();
        siteModel.setUrl(url);
        siteModel.setTitle(title);

        return siteModel;
    }
}
