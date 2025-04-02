package com.project.brka_media.controller;

import com.postmarkapp.postmark.client.exception.PostmarkException;
import com.project.brka_media.DTO.EmailDTO;
import com.project.brka_media.constants.WebConstants;
import com.project.brka_media.service.EmailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags="Test Resource")
@RestController
public class ResourceController {

    @Autowired
    private EmailService emailService;

    @PostMapping(WebConstants.PUBLIC_BASE_URL +"/contactus")
    public ResponseEntity<String> contactUs(@RequestBody EmailDTO emailDTO) throws IOException, PostmarkException {
        emailService.contactUs(emailDTO);

        return new ResponseEntity<String>("Mail send", HttpStatus.OK);
    }
}
