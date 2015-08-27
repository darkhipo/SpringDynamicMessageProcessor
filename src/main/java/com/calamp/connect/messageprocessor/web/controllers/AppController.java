/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.web.controllers;

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
