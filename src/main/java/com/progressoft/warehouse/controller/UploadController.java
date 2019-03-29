package com.progressoft.warehouse.controller;


import com.progressoft.warehouse.Service.DealService;
import com.progressoft.warehouse.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Created by EYAD on 3/10/2019.
 */
@Controller()
@RequestMapping("/dealService")
@RequestScope
public class UploadController {

    @Autowired
    DealService dealService;
    @Autowired
    FileService fileService;

    @RequestMapping(path = "/upload/", method = RequestMethod.GET)
    public String loginPage() {
        return "/FileUpload.xhtml";
    }

}
