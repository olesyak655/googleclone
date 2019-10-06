package com.spdtest.googleclone.controllers;

import com.spdtest.googleclone.models.SiteModel;
import com.spdtest.googleclone.services.SiteIndexService;
import com.spdtest.googleclone.services.SiteSearchService;
import com.spdtest.googleclone.validators.Url;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.List;

@Controller
@Validated
public class GoogleCloneController extends BaseController {

    @Inject
    private SiteSearchService siteSearchService;

    @Inject
    private SiteIndexService siteIndexService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/index")
    public String doIndex(
            @Url
            @RequestParam("q") String url,
            @RequestParam(value = "d", required = false, defaultValue = "3") int totalDepth,
            Model model) {

        model.addAttribute("q", url);
        siteIndexService.indexSite(url, totalDepth);
     //   https://spring.io/guides/gs/serving-web-content/
        return "indexresult";
    }

    @GetMapping("/")
    public String search() {
        return "search";
    }

    @PostMapping("/search")
    public String doSearch(@RequestParam("query") String query,
                           Model model) {

        model.addAttribute("query", query);

        List<SiteModel> siteModels = siteSearchService.search(query);
        model.addAttribute("siteModels", siteModels);

        return "searchresult";
    }

    @VisibleForTesting
    SiteIndexService getSiteIndexService() {
        return siteIndexService;
    }

    @VisibleForTesting
    void setSiteIndexService(SiteIndexService siteIndexService) {
        this.siteIndexService = siteIndexService;
    }

    @VisibleForTesting
    SiteSearchService getSiteSearchService() {
        return siteSearchService;
    }

    @VisibleForTesting
    void setSiteSearchService(SiteSearchService siteSearchService) {
        this.siteSearchService = siteSearchService;
    }
}
