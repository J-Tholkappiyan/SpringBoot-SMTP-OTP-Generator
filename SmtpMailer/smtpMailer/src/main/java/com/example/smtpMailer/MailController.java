package com.example.smtpMailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    private static final SecureRandom random = new SecureRandom();

    // Generates a secure 6-digit OTP
    private String generateOTP() {
        return String.format("%06d", random.nextInt(1000000)); // Ensures exactly 6 digits
    }

    @GetMapping("/form")
    public String openForm() {
        return "mail_form";
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text) {
        try {
            String otp = generateOTP();
            String message = text + "\n\nYour OTP is: " + otp;
            mailService.sendMail(to, subject, message);
            return ResponseEntity.ok("Email sent successfully with OTP to: " + to + "\nGenerated OTP: " + otp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending mail: " + e.getMessage());
        }
    }
}
