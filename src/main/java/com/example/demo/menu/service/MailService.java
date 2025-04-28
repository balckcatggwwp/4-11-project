package com.example.demo.menu.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(String toEmail, String userName, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail); 
        message.setSubject("🎬 感謝您的戲院訂餐！");
        message.setText("親愛的 " + userName + " 您好：\n\n" +
                        content + "\n\n" +
                        "感謝您使用本系統，祝您觀影愉快！\n" +
                        "-- 戲院點餐系統");

        message.setFrom("movieprojecteeit94@gmail.com"); // 可省略，預設會用設定檔裡的寄件人
        mailSender.send(message);
    }
}
