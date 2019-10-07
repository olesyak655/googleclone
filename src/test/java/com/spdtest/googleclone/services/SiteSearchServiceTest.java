package com.spdtest.googleclone.services;

import com.spdtest.googleclone.BaseWebTest;
import com.spdtest.googleclone.GoogleCloneApplication;
import com.spdtest.googleclone.exceptions.AppException;
import com.spdtest.googleclone.models.SiteModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SiteSearchServiceTest extends BaseWebTest {

    @Inject
    private ApplicationContext context;

    @Inject
    private SiteSearchService siteSearchService;

    private PropertyService mockedPropertyService;
    private PropertyService originalPropertyService;

    @Before
    public void setUp() {

        GoogleCloneApplication.setContext(context);

        SiteSearchService serviceUnderTest = context.getBean(SiteSearchService.class);

        this.originalPropertyService = serviceUnderTest.getPropertyService();
        this.mockedPropertyService = mock(PropertyService.class);
        serviceUnderTest.setPropertyService(mockedPropertyService);
    }

    @After
    public void tearDown() {
        SiteSearchService serviceUnderTest = context.getBean(SiteSearchService.class);
        serviceUnderTest.setPropertyService(originalPropertyService);
    }

    @Test
    public void testSearch() throws IOException {

        when(mockedPropertyService.getIndexPath()).thenReturn("src/test/resources/temp_test/google_clone_index");

        List<SiteModel> siteModels = siteSearchService.search("guides");

        assertEquals(1, siteModels.size());
        assertEquals(URL_1, siteModels.get(0).getUrl());
        assertEquals(TITLE_1, siteModels.get(0).getTitle());
    }

    @Test
    public void testSearchNoResult() throws IOException {

        when(mockedPropertyService.getIndexPath()).thenReturn("temp_test/google_clone_index");

        List<SiteModel> siteModels = siteSearchService.search("The sun is shining");

        assertEquals(0, siteModels.size());
    }

    @Test
    public void testSearchFailedIndexPath() throws IOException {

        when(mockedPropertyService.getIndexPath()).thenReturn("failed/index/path");

        try {
            siteSearchService.search("guides");
        } catch (AppException e) {
            assertEquals("Search by text guides is failed", e.getMessage().trim());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        }
    }
}