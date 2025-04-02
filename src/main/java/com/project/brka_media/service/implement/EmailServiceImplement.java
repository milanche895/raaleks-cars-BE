package com.project.brka_media.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.templates.TemplatedMessage;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import com.project.brka_media.DTO.EmailDTO;
import com.project.brka_media.service.EmailService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImplement implements EmailService {

    ApiClient client = Postmark.getApiClient("e98b559c-75dc-406a-98b1-eff1f373f9cb");

    @Override
    public void contactUs(EmailDTO emailDTO) throws IOException {

        try {

            // Kreiranje modela sa podacima za templejt
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("name", emailDTO.getName());
            templateModel.put("message", emailDTO.getMessage());
            templateModel.put("phone", emailDTO.getPhone());
            templateModel.put("email", emailDTO.getEmail());

            ObjectMapper objectMapper = new ObjectMapper();
            String templateModelJson = objectMapper.writeValueAsString(templateModel);

            TemplatedMessage message = new TemplatedMessage("it@buta-solution.com", "milanstojanovic895@gmail.com", 39582275);
            message.setTemplateModel(templateModel);  // OVDE setuješ mapu, ne JSON string
            message.setMessageStream("outbound");

            client.deliverMessageWithTemplate(message);

            // Ispis odgovora
            System.out.println("Email sent successfully: " + client.getDeliveryStats());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
