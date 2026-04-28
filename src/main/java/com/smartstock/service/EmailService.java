package com.smartstock.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    @Async
    public void sendLowStockAlert(String toEmail, String productName, int currentStock, @NotBlank(message = "Warehouse name is required") String warehouseName) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

            helper.setFrom("av2632301@gmail.com","SmartStock Alerts");
            helper.setTo(toEmail);
            helper.setSubject("Low Stock Alert : "+productName);

            String content = "<h3>SmartStock Inventory Notice</h3>" +
                    "<p>Warehouse Name : <b>" + warehouseName + "</b></p>" +
                    "<p>This is an automated alert regarding your stock levels.</p>" +
                    "<p>Product: <b>" + productName + "</b></p>" +
                    "<p>Current Quantity: <span style='color:red; font-weight:bold;'>" + currentStock + "</span></p>" +
                    "<p>Please log in to the dashboard to manage your inventory.</p>";

            helper.setText(content, true);
            mailSender.send(mimeMessage);
        }catch (Exception e) {
            // Log the error so you can debug SMTP issues in the console
            System.err.println("Failed to send email alert: " + e.getMessage());
        }
    }
}
