package com.calamp.connect.messageprocessor.domain.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calamp.connect.messageprocessor.Constants;

@RestController
public class AppController {

    @RequestMapping("/")
    public String index() {
        final String temp = Constants.bootOkString;
        return temp;
    }

}
