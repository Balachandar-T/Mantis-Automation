package first;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

	public static void sendMail(String excelPath, ArrayList<String> uRLs,String subject) throws Exception {

		Properties sendEmailProperties = new Properties();
		FileInputStream stream = new FileInputStream("config.properties");
		sendEmailProperties.load(stream);

		String strSendHost = sendEmailProperties.getProperty("send.mail.host");
		String strPort = sendEmailProperties.getProperty("send.mail.port");
		String strSendTls = sendEmailProperties.getProperty("send.mail.starttls.enable");
		String strAuth = sendEmailProperties.getProperty("send.mail.auth");
		String stremailSubject = sendEmailProperties.getProperty("emailSubject");
		String stremailContent_1 = sendEmailProperties.getProperty("emailContent_1");

		String strUserName = dataReader.getData(excelPath, "Username", "Email", 1);
		String strPassword = dataReader.getData(excelPath, "Password", "Email", 1);
		String strTo = dataReader.getData(excelPath, "To", "Email", 1);
		String strcc = dataReader.getData(excelPath, "CC", "Email", 1);

		String mantis_url = "";

		for (String mantis_number : uRLs) {

			mantis_url += " MT" + mantis_number + ",";
		}
		mantis_url = mantis_url.substring(0, mantis_url.length() - 1);

		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", strSendHost);
		properties.put("mail.smtp.port", strPort);
		properties.put("mail.smtp.starttls.enable", strSendTls);
		properties.put("mail.smtp.auth", strAuth);

		Session session = Session.getDefaultInstance(properties);
		String[] arrCCEmail = strcc.split(";");
		MimeMessage mime = new MimeMessage(session);
		mime.setFrom(new InternetAddress(strUserName));
		for (int i = 0; i < arrCCEmail.length; i++) {
			mime.addRecipient(Message.RecipientType.CC, new InternetAddress(arrCCEmail[i]));
		}
		mime.addRecipient(Message.RecipientType.TO, new InternetAddress(strTo));
		mime.setSubject(stremailSubject+" "+subject);
		mime.setContent(stremailContent_1 + " " + mantis_url, "text/html");

		Transport transport = session.getTransport("smtp");
		transport.connect(strSendHost, strUserName, strPassword);
		transport.sendMessage(mime, mime.getAllRecipients());
		transport.close();

		// System.out.println("Mail Sent Successfully........");
	}

}
