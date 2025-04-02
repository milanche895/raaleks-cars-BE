package com.project.brka_media.service;

import com.project.brka_media.DTO.EmailDTO;

import java.io.IOException;

public interface EmailService {
    void contactUs(EmailDTO emailDTO) throws IOException;
}
