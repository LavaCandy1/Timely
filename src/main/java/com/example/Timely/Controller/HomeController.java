package com.example.Timely.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Service.Convertors.HtmToCsvService;
import com.example.Timely.Service.Convertors.XlsToCsvService;


@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    HtmToCsvService htmToCsvService;

    @Autowired
    XlsToCsvService xlsToCsvService;
    
    @RequestMapping
    public String home() {
        return "Welcome to Timely!";
    }
    
    @RequestMapping("/htmChange")
    public String htmChange() {

        htmToCsvService.processAllHtmFiles();
        return "Changed htm to csv";
    }

    @RequestMapping("/xlsChange")
    public String xlsChange() {

        xlsToCsvService.processAllXlsFiles();
        return "Changed xls to csv";
    }
}
