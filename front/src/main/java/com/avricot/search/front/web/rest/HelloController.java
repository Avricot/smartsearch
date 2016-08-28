package com.avricot.search.front.web.rest;

import com.avricot.search.front.service.SearchDTO;
import com.avricot.search.front.service.SerializationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class HelloController {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private SerializationService searchService;

    @RequestMapping("/")
    public String index() throws Exception {
        return "online";
    }

    /**
     * Simple test.
     */
    @RequestMapping("/search")
    @ResponseBody
    public String saveSearch(@RequestParam("search") String search, @RequestHeader(value = "referer", required = false) final String referer) throws Exception {
        final SearchDTO dto = mapper.readValue(search, SearchDTO.class);
        searchService.saveSearch(dto, referer);
        return "online";
    }

}
