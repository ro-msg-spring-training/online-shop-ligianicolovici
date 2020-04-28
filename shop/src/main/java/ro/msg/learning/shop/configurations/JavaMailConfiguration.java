package ro.msg.learning.shop.configurations;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;

@Configuration
public class JavaMailConfiguration implements EnvironmentAware {
    public static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    private static final String HOST = "spring.mail.host";
    private static final String PORT = "spring.mail.port";
    private static final String PROTOCOL = "mail.server.protocol";
    private static final String USERNAME = "spring.mail.username";
    private static final String PASSWORD = "spring.mail.password";

    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty(HOST));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty(PORT))));
        mailSender.setProtocol(environment.getProperty(PROTOCOL));
        mailSender.setUsername(environment.getProperty(USERNAME));
        mailSender.setPassword(environment.getProperty(PASSWORD));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;

    }
}
