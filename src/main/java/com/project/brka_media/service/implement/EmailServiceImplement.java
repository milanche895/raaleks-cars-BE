package com.project.brka_media.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.templates.TemplatedMessage;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import com.project.brka_media.DTO.EmailDTO;
import com.project.brka_media.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            templateModel.put("email", emailDTO.getEmail());
            templateModel.put("message", emailDTO.getMessage());
            templateModel.put("subject", emailDTO.getSubject());
            templateModel.put("firstRegistration", emailDTO.getFirstRegistration());
            templateModel.put("brand", emailDTO.getBrand());
            templateModel.put("model", emailDTO.getModel());
            templateModel.put("mileage", emailDTO.getMileage());
            templateModel.put("zipCode", emailDTO.getVehicleLocationZipCode());
            templateModel.put("email", emailDTO.getEmailAddress());
            templateModel.put("phone", emailDTO.getPhoneNumber());
            templateModel.put("firstName", emailDTO.getFirst_name());
            templateModel.put("zipCode", emailDTO.getPlz());
            templateModel.put("birthday", emailDTO.getBirthday());
            templateModel.put("phone", emailDTO.getPhone());
            templateModel.put("country", emailDTO.getCountry_of_origin());
            templateModel.put("residence", emailDTO.getResidence_permit());
            templateModel.put("typeCertificate", emailDTO.getType_certificate());
            templateModel.put("stateNumber", emailDTO.getState_number());
            templateModel.put("marketPlace", emailDTO.getPlacing_on_the_market());
            templateModel.put("km", emailDTO.getKm());
            templateModel.put("lastInsurance", emailDTO.getYour_last_insurance());
            templateModel.put("desireCoverage", emailDTO.getLiability());
            templateModel.put("email", emailDTO.getEmail());
            ObjectMapper objectMapper = new ObjectMapper();
            String templateModelJson = objectMapper.writeValueAsString(templateModel);
            Integer templateId = null;
            if (emailDTO.getTypeMessage().equals("contact")){
                templateId = 39582275;
            } else if (emailDTO.getTypeMessage().equals("insurance")){
                templateId = 39585451;
            } else if (emailDTO.getTypeMessage().equals("sell")){
                templateId = 39585486;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please insert typeMessage: contact, sell or insurance!");
            }
            TemplatedMessage message = new TemplatedMessage("it@buta-solution.com", "milanstojanovic895@gmail.com", templateId);
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
