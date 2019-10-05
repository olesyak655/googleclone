package com.spdtest.googleclone;

import com.spdtest.googleclone.models.SiteModel;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = GoogleCloneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "test")
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class})
@Ignore
public class BaseWebTest implements TestConstants {

    public SiteModel createSiteModel(String url, String title) {

        SiteModel siteModel = new SiteModel();
        siteModel.setUrl(url);
        siteModel.setTitle(title);

        return siteModel;
    }
}
