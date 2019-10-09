package com.spdtest.googleclone.controllers;

import com.spdtest.googleclone.exceptions.AppException;
import com.spdtest.googleclone.models.SiteModel;
import com.spdtest.googleclone.services.SiteIndexService;
import com.spdtest.googleclone.services.SiteSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GoogleCloneController.class)
@ActiveProfiles(profiles = "test")
public class GoogleCloneControllerTest {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String URL_1 = "https://mysite.io/guides/";
    private static final String URL_2 = "https://mysite.io/";
    private static final String URL_INCORRECT = "Bad_url";

    private static final String TITLE_1 = "MySite guide";
    private static final String TITLE_2 = "MySite";

    private static final String PARAM_QUERY = "query";
    private static final String PARAM_QUERY_VALUE = "SpringBoot";

    @MockBean
    private SiteIndexService mockedSiteIndexService;
    @MockBean
    private SiteSearchService mockedSiteSearchService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
    }

    @Test
    public void testIndex() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(SERVER_URL + "/index"))
                .andExpect(forwardedUrl("/WEB-INF/views/index.jsp"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDoIndex() throws Exception {

        doNothing().when(mockedSiteIndexService).indexSite(URL_1, 2);

        mockMvc.perform(MockMvcRequestBuilders.post(SERVER_URL + "/index")
                .param("q", URL_1)
                .param("d", "2"))
                .andExpect(forwardedUrl("/WEB-INF/views/indexresult.jsp"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDoIndexIncorrectUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(SERVER_URL + "/index")
                .param("q", URL_INCORRECT)
                .param("d", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearch() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(SERVER_URL + "/"))
                .andExpect(forwardedUrl("/WEB-INF/views/search.jsp"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDoSearch() throws Exception {

        SiteModel siteModel1 = createSiteModel(URL_1, TITLE_1);
        SiteModel siteModel2 = createSiteModel(URL_2, TITLE_2);
        List<SiteModel> siteModels = Arrays.asList(new SiteModel[] {siteModel1, siteModel2});

        when(mockedSiteSearchService.search(PARAM_QUERY_VALUE)).thenReturn(siteModels);

        mockMvc.perform(MockMvcRequestBuilders.post(SERVER_URL + "/search")
                .param(PARAM_QUERY, PARAM_QUERY_VALUE))
                .andExpect(forwardedUrl("/WEB-INF/views/searchresult.jsp"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDoSearchIsFailed() throws Exception {

        doThrow(new AppException(HttpStatus.NOT_FOUND, "Search by text " + PARAM_QUERY_VALUE + " is failed"))
                .when(mockedSiteSearchService).search(PARAM_QUERY_VALUE);

        mockMvc.perform(MockMvcRequestBuilders.post(SERVER_URL + "/search")
                .param(PARAM_QUERY, PARAM_QUERY_VALUE))
                .andExpect(status().isNotFound());
    }

    private SiteModel createSiteModel(String url, String title) {

        SiteModel siteModel = new SiteModel();
        siteModel.setUrl(url);
        siteModel.setTitle(title);

        return siteModel;
    }
}


