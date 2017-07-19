package testBlog.config.communication;


import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class BlogMailSEnder  {

    private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    public BlogMailSEnder() {
        this.mailSender = new MailSender() {
            @Override
            public void send(SimpleMailMessage simpleMailMessage) throws MailException {
                System.out.println("Execution");
            }

            @Override
            public void send(SimpleMailMessage... simpleMailMessages) throws MailException {
                System.out.println("Execution");
            }
        };
        this.templateMessage  = new SimpleMailMessage();
    }

    public void sendMailToAddress(String address,String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage(this.templateMessage);
        message.setTo(address);
        message.setText("Dear user ( " + address + " ) your password address was changed to: " + newPassword );
        try {
            this.mailSender.send(message);
        } catch (MailException mailException) {
            System.out.println(mailException);
        }
    }
}
