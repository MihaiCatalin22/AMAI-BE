package prjcb04.amaiproject2024.business.Implementation;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import prjcb04.amaiproject2024.domain.User;
import prjcb04.amaiproject2024.persistence.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmailSender {
@Autowired
    JavaMailSender mailSender;

    UserRepository userRepository;

    AvailableTuesdays availableTuesdays;

    private final String toAddress = "a.yordanov@student.fontys.nl";
    private final String fromAddress = "vrachkapoznavachka@gmail.com";
    private final String senderName = "AMAI";
    private final String subject = "Automatic invitations email";

    public void sendInvitesYearly() {
        List<String> emailAddresses = userRepository.findAll().stream().map(User::getEmail).toList();
        var dates = availableTuesdays.generate();

        String emailAddressesString = String.join(";", emailAddresses);
        String datesString = dates.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        String content = emailAddressesString + "/" + datesString;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sendInvitesFirstPeriod() {
        List<String> emailAddresses = userRepository.findAll().stream().map(User::getEmail).toList();
        var dates = availableTuesdays.firstEducationalPeriod();

        String emailAddressesString = String.join(";", emailAddresses);
        String datesString = dates.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        String content = emailAddressesString + "/" + datesString;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);
              mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sendInvitesSecondPeriod() {
        List<String> emailAddresses = userRepository.findAll().stream().map(User::getEmail).toList();
        var dates = availableTuesdays.secondEducationalPeriod();

        String emailAddressesString = String.join(";", emailAddresses);
        String datesString = dates.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        String content = emailAddressesString + "/" + datesString;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);
              mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
