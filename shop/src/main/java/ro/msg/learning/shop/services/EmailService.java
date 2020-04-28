package ro.msg.learning.shop.services;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.utils.OrderContent;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final String EMAIL_TEXT_TEMPLATE_NAME = "email-plain-text.html";
    private static final String EMAIL_HTML_TEMPLATE_NAME = "email-html-body.html";
    public static final String PLAIN_TEXT_EMAIL = "plain-text";
    public static final String HTML_BODY_EMAIL = "html-body";

    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;

    @Value("${spring.mail.subject}")
    private String subject;


    @SneakyThrows
    public void sendMail(String toWhom, OrderContent orderContent, OrderDTO orderDTO, String mailType) {

        Context ctx = prepareContext(orderContent, orderDTO);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(subject);
        message.setTo(toWhom);

        switch (mailType) {
            case PLAIN_TEXT_EMAIL:
                String textContent = templateEngine.process(EMAIL_TEXT_TEMPLATE_NAME, ctx);
                message.setText(textContent);
                break;
            case HTML_BODY_EMAIL:
                String htmlContent = templateEngine.process(EMAIL_HTML_TEMPLATE_NAME, ctx);
                message.setText(htmlContent, true);
                break;
            default:
        }

        emailSender.send(mimeMessage);
    }


    private Context prepareContext(OrderContent orderContent, OrderDTO orderDTO) {
        Context ctx = new Context();
        ctx.setVariable("orderContent", orderContent);
        ctx.setVariable("orderEntity", orderDTO);
        return ctx;
    }

}
