package com.spdtest.googleclone.controllers;

import com.spdtest.googleclone.models.SiteModel;
import com.spdtest.googleclone.services.SiteIndexService;
import com.spdtest.googleclone.services.SiteSearchService;
import com.spdtest.googleclone.validators.Url;
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
public class GoogleCloneController {

    private SiteSearchService siteSearchService;
    private SiteIndexService siteIndexService;

    @Inject
    public GoogleCloneController(SiteSearchService siteSearchService, SiteIndexService siteIndexService) {
        this.siteSearchService = siteSearchService;
        this.siteIndexService = siteIndexService;
    }

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
}
