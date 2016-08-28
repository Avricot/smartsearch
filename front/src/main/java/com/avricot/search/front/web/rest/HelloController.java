package com.avricot.search.front.web.rest;

import com.avricot.search.front.service.SearchDTO;
import com.avricot.search.front.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class HelloController {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private SearchService searchService;

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
