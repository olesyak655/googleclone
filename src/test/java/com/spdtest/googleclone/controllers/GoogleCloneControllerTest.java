package com.spdtest.googleclone.controllers;

import com.spdtest.googleclone.BaseWebTest;
import com.spdtest.googleclone.GoogleCloneApplication;
import com.spdtest.googleclone.models.SiteModel;
import com.spdtest.googleclone.services.SiteIndexService;
import com.spdtest.googleclone.services.SiteSearchService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GoogleCloneControllerTest extends BaseWebTest {

    @Inject
    private WebApplicationContext context;

    private SiteIndexService mockedSiteIndexService;
    private SiteIndexService originalSiteIndexService;

    private SiteSearchService mockedSiteSearchService;
    private SiteSearchService originalSiteSearchService;

    private MockMvc mvc;

    @Before
    public void setUp() {

        GoogleCloneApplication.setContext(context);

        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        GoogleCloneController controllerUnderTest = context.getBean(GoogleCloneController.class);

        this.originalSiteIndexService = controllerUnderTest.getSiteIndexService();
        this.mockedSiteIndexService = mock(SiteIndexService.class);
        controllerUnderTest.setSiteIndexService(mockedSiteIndexService);

        this.originalSiteSearchService = controllerUnderTest.getSiteSearchService();
        this.mockedSiteSearchService = mock(SiteSearchService.class);
        controllerUnderTest.setSiteSearchService(mockedSiteSearchService);

    }

    @After
    public void tearDown() {
        GoogleCloneController controllerUnderTest = context.getBean(GoogleCloneController.class);
        controllerUnderTest.setSiteIndexService(originalSiteIndexService);
        controllerUnderTest.setSiteSearchService(originalSiteSearchService);
    }

    @Test
    public void testIndex() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(SERVER_URL + "/index"))
                .andExpect(forwardedUrl("/WEB-INF/views/index.jsp"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDoIndex() throws Exception {

        doNothing().when(mockedSiteIndexService).indexSite(URL_1, 2);

        mvc.perform(MockMvcRequestBuilders.post(SERVER_URL + "/index")
                .param("q", URL_1)
                .param("d", "2"))
                .andExpect(forwardedUrl("/WEB-INF/views/indexresult.jsp"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearch() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(SERVER_URL + "/"))
                .andExpect(forwardedUrl("/WEB-INF/views/search.jsp"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDoSearch() throws Exception {

        SiteModel siteModel1 = createSiteModel(URL_1, TITLE_1);
        SiteModel siteModel2 = createSiteModel(URL_2, TITLE_2);
        List<SiteModel> siteModels = Arrays.asList(new SiteModel[] {siteModel1, siteModel2});

        when(mockedSiteSearchService.search(PARAM_QUERY_VALUE)).thenReturn(siteModels);

        mvc.perform(MockMvcRequestBuilders.post(SERVER_URL + "/search")
                .param(PARAM_QUERY, PARAM_QUERY_VALUE))
                .andExpect(forwardedUrl("/WEB-INF/views/searchresult.jsp"))
                .andExpect(status().isOk());
    }
}
