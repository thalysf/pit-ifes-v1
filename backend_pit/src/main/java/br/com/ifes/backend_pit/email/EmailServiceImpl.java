package br.com.ifes.backend_pit.email;

import br.com.ifes.backend_pit.exception.EmailException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${send.grid.apikey}")
    private String apikey;

    @Override
    public void enviarEmail(
            String para, String assunto, String mensagem) {

        Email from = new Email("thalysfenix@gmail.com");
        Email to = new Email(para);
        String subject = assunto;
        Content content = new Content("text/plain", mensagem);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(apikey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
    }
}
