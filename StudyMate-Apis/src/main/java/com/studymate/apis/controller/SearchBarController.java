package com.studymate.apis.controller;

import com.studymate.apis.constansts.URLMappingConstants;
import com.studymate.dtos.SearchResultDto;
import com.studymate.service.SearchBarService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(URLMappingConstants.SEARCH_BAR)
@RestController
public class SearchBarController {
    private static final Logger log = LogManager.getLogger(SearchBarController.class);
    @Autowired
    private SearchBarService searchBarService;

    @GetMapping
    public List<SearchResultDto> search(@RequestParam String query) {
        log.info("Searching for {}", query);
        return searchBarService.search(query);
    }

}
