package com.spdtest.googleclone.services;

import com.spdtest.googleclone.exceptions.AppException;
import com.spdtest.googleclone.models.SiteModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class SiteSearchServiceTest {

    private static final String URL_1 = "https://mysite.io/guides/";
    private static final String TITLE_1 = "MySite guide";

    private SiteSearchService siteSearchService;
    private PropertyService mockedPropertyService;

    @Before
    public void setUp() {

        this.mockedPropertyService = mock(PropertyService.class);
        siteSearchService = new SiteSearchService(mockedPropertyService);
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