package com.spdtest.googleclone.services;

import com.spdtest.googleclone.BaseWebTest;
import com.spdtest.googleclone.GoogleCloneApplication;
import com.spdtest.googleclone.models.SiteModel;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SiteIndexServiceTest extends BaseWebTest {

    @Inject
    private ApplicationContext context;

    @Inject
    private SiteIndexService siteIndexService;

    @Inject
    private PropertyService propertyService;

    private SiteParseService mockedSiteParseService;
    private SiteParseService originalSiteParseService;

    @Before
    public void setUp() {

        GoogleCloneApplication.setContext(context);

        SiteIndexService serviceUnderTest = context.getBean(SiteIndexService.class);

        this.originalSiteParseService = serviceUnderTest.getSiteParseService();
        this.mockedSiteParseService = mock(SiteParseService.class);
        serviceUnderTest.setSiteParseService(mockedSiteParseService);
    }

    @After
    public void tearDown() {
        SiteIndexService serviceUnderTest = context.getBean(SiteIndexService.class);
        serviceUnderTest.setSiteParseService(originalSiteParseService);
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

        doReturn("Content from site spring.io").doReturn("Content from site spring.io/guides with guides")
                .when(mockedSiteParseService).generateText(any());

        siteIndexService.indexSite(URL_1, 2);

        assertTrue(new File(propertyService.getIndexPath()).exists());
    }
}
