package com.horeca.site.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController
{
    //sample endpoint that doesn't require authentication
    @ResponseBody
    @RequestMapping("/anonymous")
    public String anonymous() {
        return "Some content returned to an anonymous user";
    }

    //users must be authenticated (via OAuth2) to access this sample endpoint
    @ResponseBody
    @RequestMapping("/restricted")
    public String restricted() {
        return "Some content returned to authenticated users only";
    }
}
