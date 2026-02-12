package com.fss.maps.common.util;

import static com.fss.maps.common.util.MAPSUtils.nullOrEmpty;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fss.maps.batch.gen.genCommon.GenObjectPool;
import com.fss.maps.batch.mps.mpsConstants;
import com.fss.maps.common.bean.EmailAPIRequest;
import com.fss.maps.common.bean.EmailAPIResponse;
import com.fss.maps.common.bean.EmailBean;
import com.fss.maps.common.bean.SessionBean;
import com.fss.maps.common.dao.EmailServiceDAO;
import com.fss.maps.common.services.ParameterValuesService;
import com.fss.maps.common.services.ParameterValuesServiceImpl;
import com.fss.maps.common.util.logger.LoggerUtil;

import oracle.jdbc.logging.annotations.Log;

import com.fss.maps.common.pathvalidation.PathCleaner;
import com.fss.maps.common.pathvalidation.PathTraversalDetector;
import com.fss.maps.common.pathvalidation.PathValidator;

/**
 * @author Ewan Drego
 * @Date   May 7, 2010
 * @Time   10:37:11 AM
 * @Purpose : Email Service Mechanism.
 * 
 * @version 1.1 Supports disabling email sending. To disable email sending, set SMTP host name as null (PARM_KEY=2)	
 */
public class EmailService {
	
	private JavaMailSenderImpl mailSender=null;
	private String text=null;
	private String subject=null;
	private String from=null;
	private int priority;
	private String errorMessage=null;
	public static Integer SMTP_KEY=2; //default SMTP host name key is 2.
	private static int FROM_ADD_KEY = 5 ; //default From Address Key is 5.
	private EmailServiceDAO emailServiceDao=null;
	private ParameterValuesServiceImpl parameterValuesServiceImpl=null;
	private VelocityEngine velocityEngine=null;
	private static final LoggerUtil LOG = new LoggerUtil(EmailService.class);
	public static Integer SMTP_PORT=43; //default SMTP port key is 43.
	public static String EMAIL_PATH; // Email path Key
	private static mpsConstants mpsConstantsInstance;
	
	private static final RestTemplate restTemplate = new RestTemplate();
	private Map<String, String> headersMap;
	
	/*
	 * public VelocityEngine getVelocityEngine() { return velocityEngine; }
	 * 
	 * public void setVelocityEngine(VelocityEngine velocityEngine) {
	 * this.velocityEngine = velocityEngine; }
	 */

/**Added by Logesh for PathManipulation fix*/
	PathCleaner cleaner = new PathCleaner();
    PathTraversalDetector detector = new PathTraversalDetector();
    PathValidator validator = new PathValidator(cleaner, detector);

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public ParameterValuesServiceImpl getParameterValuesServiceImpl() {
		return parameterValuesServiceImpl;
	}

	public void setParameterValuesServiceImpl(ParameterValuesServiceImpl parameterValuesServiceImpl) {
		this.parameterValuesServiceImpl = parameterValuesServiceImpl;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}


	/**
	 * @param emailServiceDao the emailServiceDao to set
	 */
	public EmailServiceDAO getEmailServiceDao() {
		return emailServiceDao;
	}
	public void setEmailServiceDao(EmailServiceDAO emailServiceDao) {
		this.emailServiceDao=emailServiceDao;
	}
	
	public Map<String, String> getHeadersMap() {
		return headersMap;
	}

	public void setHeadersMap(Map<String, String> headersMap) {
		this.headersMap = headersMap;
	}

	public static synchronized mpsConstants getMpsConstantsInstance() {
		if (mpsConstantsInstance == null) {
			mpsConstantsInstance = GenObjectPool.getInstance().getMpsConstantsObj(101);
		}
		return mpsConstantsInstance;
	}
	
	


	public void sendEmail(Map<String, String> map, String emailID) throws Exception {
		try {
			String hostName = null;
			String portNumber = null;
			String osname = System.getProperty("os.name");
			try {
				
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Starts
				ApplicationContext applicationContext = CustomApplicationContext.getApplicationContext();
				ParameterValuesService paramValueService = (ParameterValuesService) applicationContext.getBean("parameterValuesServiceImpl");
				/**if (osname.startsWith("Windows")) {
					EMAIL_PATH ="C:/DIB_MAPSCore/Config_1/mailTemplate/";
				} else {**/
					EMAIL_PATH = paramValueService.getGenrParamValue(MapsConstants.SYSTEM_EMAIL_PATH);
				//}
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Ends
				
				// Get the SMTP host name from the db.
				hostName = emailServiceDao.getHost(SMTP_KEY).trim();
				portNumber = emailServiceDao.getHost(SMTP_PORT).trim();
			} catch (NullPointerException npe) {
				LOG.error("Emailing is disabled");
			}
			if (StringUtils.isEmpty(hostName)) {
				LOG.info("Emailing is disabled. To enable, configure the mail server host name in db");
			}else if(StringUtils.isEmpty(portNumber)){
				LOG.info("Emailing is disabled. To enable, configure the mail server port in db");
			}else if(!MAPSUtils.isNumeric(portNumber)){
				LOG.info("Emailing is disabled. To enable, configure valid mail server port in db - Numeric Only");
			}else {
				mailSender.setHost(hostName);
				mailSender.setPort(Integer.parseInt(portNumber));
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper mime = new MimeMessageHelper(message, true);
				String text = getText();
				Iterator<String> it = map.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					text = text.replace("(" + key + ")", map.get(key));
				}
				String[] email = emailID.split(",");
				mime.setFrom(emailServiceDao.getFromAddress(FROM_ADD_KEY).trim());
				mime.setTo(email);
				mime.setSubject(getSubject());
				//mime.setText(text);
				mime.setText("Testing");
				mime.setPriority(getPriority());
				
				//ST:vikas
				LOG.info("hostName"+hostName);
				LOG.info("portNumber"+portNumber);
				LOG.info("text"+text);
				LOG.info("email[0]"+email[0]);
				LOG.info("emailServiceDao.getFromAddress(FROM_ADD_KEY).trim()"+emailServiceDao.getFromAddress(FROM_ADD_KEY).trim());
				LOG.info("getSubject()"+getSubject());
				LOG.info("getPriority()"+getPriority());
				//EN:vikas
				mailSender.send(message);
			}
		} catch (Exception e) {
			LOG.error("Failed to send email--", e);
			throw new Exception("E-mail to Recipient Failed ,Contact System Administrator ");
		}
	}
	
	/**
	 * Sends email using the details given in the emailBean
	 * 
	 * @param emailBean
	 * @throws MessagingException
	 */
	public void sendEmail(EmailBean emailBean) throws Exception {
		try {
			String hostName =null;
			String portNumber = null;
			String text=null;
			String osname = System.getProperty("os.name");
			try {
				
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Starts
				ApplicationContext applicationContext = CustomApplicationContext.getApplicationContext();
				ParameterValuesService paramValueService = (ParameterValuesService) applicationContext.getBean("parameterValuesServiceImpl");
				if (osname.startsWith("Windows")) {
					EMAIL_PATH ="C:/DIB_MAPSCore/Config_1/mailTemplate/";
				} else {
					EMAIL_PATH = paramValueService.getGenrParamValue(MapsConstants.SYSTEM_EMAIL_PATH);
				}
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Ends
				
				hostName = emailServiceDao.getHost(SMTP_KEY);
				portNumber = emailServiceDao.getHost(SMTP_PORT).trim();
			} catch (NullPointerException npe) {
				
				LOG.error("Emailing is disabled"+npe.getMessage());
			}
			if (StringUtils.isEmpty(hostName)) {
				LOG.info("Emailing is disabled. To enable, configure the mail server host name in db");
			}else if(StringUtils.isEmpty(portNumber)){
				LOG.info("Emailing is disabled. To enable, configure the mail server port in db");
			}else if(!MAPSUtils.isNumeric(portNumber)){
				LOG.info("Emailing is disabled. To enable, configure valid mail server port in db - Numeric Only");
			}else {
				mailSender.setHost(hostName);
				mailSender.setPort(Integer.parseInt(portNumber));
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper mime = new MimeMessageHelper(message, true, MapsConstants.CHARACTER_ENCODING);
				LOG.info("emailBean.isDefaultFromRequired()"+emailBean.isDefaultFromRequired());
				if (emailBean.isDefaultFromRequired()) {
					
					emailBean.setFrom(emailServiceDao.getFromAddress(FROM_ADD_KEY).trim());
				}
				
				String[] fromMail=emailBean.getFrom().split(",");
				mime.setFrom(fromMail[0]);
				if(emailBean.getTo().contains(","))
				{
				mime.setTo(emailBean.getTo().split(","));
				}
				else
				{
					mime.setTo(emailBean.getTo());
				}
				if (!nullOrEmpty(emailBean.getCc())) {
					mime.setCc(emailBean.getCc().split(","));
				}
				
				// Start:Gaurav Logger added as requested by SIT team
				LOG.info("hostName::"+hostName);
				LOG.info("portNumber::"+portNumber);
				LOG.info("emailBean.isDefaultFromRequired()::"+emailBean.isDefaultFromRequired());
				LOG.info("fromMail[0]::"+fromMail[0]);
				LOG.info("emailBean.getTo()::"+emailBean.getTo());
				LOG.info("emailBean.getCc()::"+emailBean.getCc());
				LOG.info("getPriority()::"+getPriority());
				LOG.info("emailBean.getSubject()::"+emailBean.getSubject());
			
				// End:Gaurav

				mime.setPriority(getPriority());
				mime.setSubject(emailBean.getSubject());
				if(emailBean.getVelocityTemplateName()!=null){
					//text = VelocityEngine.mergeTemplateIntoString(getFileResourceLoaderEngine(), emailBean.getVelocityTemplateName(), MapsConstants.CHARACTER_ENCODING, emailBean.getBodyVariables());
					velocityEngine = getFileResourceLoaderEngine();
					velocityEngine.init();		
			        VelocityContext context = new VelocityContext(emailBean.getBodyVariables());
			        Template template = velocityEngine.getTemplate(emailBean.getVelocityTemplateName());    
			        StringWriter writer = new StringWriter();
			        template.merge(context, writer);
			        text = writer.toString();
				}else{
					text=emailBean.getText();
				}
				
				mime.setText(text, true);
				mailSender.send(message);
			}
		}catch(Exception e ){
			LOG.error("Failed to send email", e);
			throw e; 
		}
	}
	
	
	
	//Praveen - Begin
	public boolean sendEmailTest(EmailBean emailBean) throws Exception {
		
		boolean methodSuccess = false;
		try {
			String hostName =null;
			
			String text=null;
			String osname = System.getProperty("os.name");
			try {
				
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Starts
				ApplicationContext applicationContext = CustomApplicationContext.getApplicationContext();
				ParameterValuesService paramValueService = (ParameterValuesService) applicationContext.getBean("parameterValuesServiceImpl");
				/**if (osname.startsWith("Windows")) {
					EMAIL_PATH ="C:/DIB_MAPSCore/Config_1/mailTemplate/";
				} else {**/
					EMAIL_PATH = paramValueService.getGenrParamValue(MapsConstants.SYSTEM_EMAIL_PATH);
				//}
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Ends
				
				hostName = emailServiceDao.getHost(SMTP_KEY);
			} catch (NullPointerException npe) {
				//npe.printStackTrace();
				LOG.error("Emailing is disabled");
			}
			if (StringUtils.isEmpty(hostName)) {
				LOG.info("Emailing is disabled. To enable, configure the mail server host name in db");
			} else {
				mailSender.setHost(hostName);
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper mime = new MimeMessageHelper(message, true, MapsConstants.CHARACTER_ENCODING);

				if (emailBean.isDefaultFromRequired()) {
					SessionBean sessionBean = (SessionBean) CustomApplicationContext.getApplicationContext().getBean("sessionBean");
					emailBean.setFrom(sessionBean.getInstName());
				}
				
				String[] fromMail=emailBean.getFrom().split(",");
				mime.setFrom(fromMail[0]);
				mime.setTo(emailBean.getTo().split(","));
				if (!nullOrEmpty(emailBean.getCc())) {
					mime.setCc(emailBean.getCc().split(","));
				}
				/*//ST:vikas
				LOG.info("hostName::"+hostName);
				LOG.info("portNumber::"+portNumber);
				LOG.info("emailBean.isDefaultFromRequired()::"+emailBean.isDefaultFromRequired());
				LOG.info("fromMail[0]::"+fromMail[0]);
				LOG.info("emailBean.getTo()::"+emailBean.getTo());
				LOG.info("emailBean.getCc()::"+emailBean.getCc());
				LOG.info("getPriority()::"+getPriority());
				LOG.info("emailBean.getSubject()::"+emailBean.getSubject());
				//EN:vikas
*/				mime.setPriority(getPriority());
				mime.setSubject(emailBean.getSubject());
				if(emailBean.getVelocityTemplateName()!=null){
					//text = VelocityEngineUtils.mergeTemplateIntoString(getFileResourceLoaderEngine(), emailBean.getVelocityTemplateName(), MapsConstants.CHARACTER_ENCODING, emailBean.getBodyVariables());
					velocityEngine = getFileResourceLoaderEngine();
					velocityEngine.init();		
			        VelocityContext context = new VelocityContext(emailBean.getBodyVariables());
			        Template template = velocityEngine.getTemplate(emailBean.getVelocityTemplateName());    
			        StringWriter writer = new StringWriter();
			        template.merge(context, writer);
			        text = writer.toString();
				}else{
					text=emailBean.getText();
				}
				//String text="Testing for forgotpassword";
				LOG.info("text"+text);
				mime.setText(text, true);
				methodSuccess = true;
			}
		}
		catch(ResourceNotFoundException re ){
			LOG.error("Failed to send email", re);
			throw new Exception("E-mail to Recipient Failed ,Contact System Administrator "+re.getMessage()); 
		}catch(MailSendException me ){
			LOG.error("Mail Server Connection Failed:", me);
			throw new Exception("Mail Server Connection Failed"); 
		}
		catch(Exception e ){
			LOG.error("Failed to send email", e);
			throw new Exception("E-mail to Recipient Failed ,Contact System Administrator "+e); 
		}
		return methodSuccess;
	}
	
	/**
	 * Used to Send email with inline image in the mail body
	 * @param emailBean
	 * @param instId
	 * @throws Exception
	 */
	public void sendEmailWithInlineImage(EmailBean emailBean, String imagePath) throws Exception {
		LOG.logMethodEntry("sendEmailWithInlineImage");
		try {
			 String hostName =null;
			 String portNumber = null;
			 String text=null;
			 String osname = System.getProperty("os.name");
			try{
				
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Starts
				ApplicationContext applicationContext = CustomApplicationContext.getApplicationContext();
				ParameterValuesService paramValueService = (ParameterValuesService) applicationContext.getBean("parameterValuesServiceImpl");
				/**if (osname.startsWith("Windows")) {
					EMAIL_PATH ="C:/DIB_MAPSCore/Config_1/mailTemplate/";
				} else {**/
					EMAIL_PATH = paramValueService.getGenrParamValue(MapsConstants.SYSTEM_EMAIL_PATH);
				//}
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Ends
				
				hostName = emailServiceDao.getHost(SMTP_KEY);
				portNumber = emailServiceDao.getHost(SMTP_PORT).trim();
			} catch (NullPointerException npe) {
				//npe.printStackTrace();
				LOG.error("Emailing is disabled");
			}
			if (StringUtils.isEmpty(hostName)) {
				LOG.info("Emailing is disabled. To enable, configure the mail server host name in db");
			}else if(StringUtils.isEmpty(portNumber)){
				LOG.info("Emailing is disabled. To enable, configure the mail server port in db");
			}else if(!MAPSUtils.isNumeric(portNumber)){
				LOG.info("Emailing is disabled. To enable, configure valid mail server port in db - Numeric Only");
			}else {
				mailSender.setHost(hostName);
				mailSender.setPort(Integer.parseInt(portNumber));
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper mime = new MimeMessageHelper(message, true, MapsConstants.CHARACTER_ENCODING);
				LOG.info("emailBean.isDefaultFromRequired()"+emailBean.isDefaultFromRequired());
				if (emailBean.isDefaultFromRequired()) {
					//SessionBean sessionBean = (SessionBean) CustomApplicationContext.getApplicationContext().getBean("sessionBean");
					//emailBean.setFrom(sessionBean.getInstName());
					emailBean.setFrom(emailServiceDao.getFromAddress(FROM_ADD_KEY).trim());
				}
				
				String[] fromMail=emailBean.getFrom().split(",");
				mime.setFrom(fromMail[0]);
				mime.setTo(emailBean.getTo().split(","));
				if (!nullOrEmpty(emailBean.getCc())) {
					mime.setCc(emailBean.getCc().split(","));
				}
				
				//ST:vikas
				LOG.info("hostName::"+hostName);
				LOG.info("portNumber::"+portNumber);
				LOG.info("emailBean.isDefaultFromRequired()::"+emailBean.isDefaultFromRequired());
				LOG.info("fromMail[0]::"+fromMail[0]);
				LOG.info("emailBean.getTo()::"+emailBean.getTo());
				LOG.info("emailBean.getCc()::"+emailBean.getCc());
				LOG.info("getPriority()::"+getPriority());
				LOG.info("emailBean.getSubject()::"+emailBean.getSubject());
				//EN:vikas
				mime.setPriority(getPriority());
				mime.setSubject(emailBean.getSubject());
				if(emailBean.getVelocityTemplateName()!=null){
					//text = VelocityEngineUtils.mergeTemplateIntoString(getFileResourceLoaderEngine(), emailBean.getVelocityTemplateName(), MapsConstants.CHARACTER_ENCODING, emailBean.getBodyVariables());
					velocityEngine = getFileResourceLoaderEngine();
					velocityEngine.init();		
			        VelocityContext context = new VelocityContext(emailBean.getBodyVariables());
			        Template template = velocityEngine.getTemplate(emailBean.getVelocityTemplateName());    
			        StringWriter writer = new StringWriter();
			        template.merge(context, writer);
			        text = writer.toString();
					 
				}else{
					text=emailBean.getText();
				}
				LOG.info("text"+text);
				mime.setText(text, true);
				// first part (the html)
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				
				messageBodyPart.setContent(text, "text/html");
				// add it
				MimeMultipart multipart = new MimeMultipart("related");
				multipart.addBodyPart(messageBodyPart);
				
				// second part (the image)
		        messageBodyPart = new MimeBodyPart();
		        DataSource fds = new FileDataSource(new File(imagePath));

		        messageBodyPart.setDataHandler(new DataHandler(fds));
		        messageBodyPart.setDisposition(MimeBodyPart.INLINE);
		        messageBodyPart.setHeader("Content-ID", "image"); // add <img src="cid:image" align="right" /> in html whereever image is required

		        // add image to the multipart
		        multipart.addBodyPart(messageBodyPart);

		        // put everything together
		        message.setContent(multipart);
		         
				mailSender.send(message);
			}
			LOG.logMethodExit("sendEmailWithInlineImage");
		}
		catch(ResourceNotFoundException re ){
			LOG.error("Failed to send email with inline image", re);
			throw new Exception("E-mail to Recipient Failed ,Contact System Administrator "+re.getMessage()); 
		}catch(MailSendException me ){
			LOG.error("Mail Server Connection Failed:", me);
			throw new Exception("Mail Server Connection Failed"); 
		}
		catch(Exception e ){
			LOG.error("Failed to send email with inline image", e);
			throw new Exception("E-mail to Recipient Failed ,Contact System Administrator "+e); 
		}
	}
	
	/**
	 * Create a File resource loader velocity engine.
	 * @author Brenden Pereira
	 * @return VelocityEngine
	 * @throws Exception
	 */
	private VelocityEngine getFileResourceLoaderEngine() throws Exception {
		VelocityEngine fileResourceLoaderEngine = null;
		try{
			fileResourceLoaderEngine = new VelocityEngine();
			fileResourceLoaderEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
			fileResourceLoaderEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, parameterValuesServiceImpl.getGenrParamValue("37"));
			fileResourceLoaderEngine.setProperty(RuntimeConstants.ENCODING_DEFAULT, "UTF-8");
			return fileResourceLoaderEngine;
		}catch(Exception e){
			LOG.error("Error getting FileResourceLoaderEngine");
			throw e;
		}
		
	}
	
	
	public void sendEmailWithAttachments(EmailBean emailBean) throws Exception {
		try {
			 String hostName =null;
			 String text=null;
			 String aryAttachFilePath[] = null;
			 MimeMultipart mp = new MimeMultipart();
			 String osname = System.getProperty("os.name");
			try {
				
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Starts
				ApplicationContext applicationContext = CustomApplicationContext.getApplicationContext();
				ParameterValuesService paramValueService = (ParameterValuesService) applicationContext.getBean("parameterValuesServiceImpl");
				/**if (osname.startsWith("Windows")) {
					EMAIL_PATH ="C:/DIB_MAPSCore/Config_1/mailTemplate/";
				} else {**/
					EMAIL_PATH = paramValueService.getGenrParamValue(MapsConstants.SYSTEM_EMAIL_PATH);
				//}
				// Code Added for Email Path Configuration - Supports both Windows and Linux - Ends
				
				hostName = emailServiceDao.getHost(SMTP_KEY);
			} catch (NullPointerException npe) {
				LOG.error("Emailing is disabled");
			}
			if (StringUtils.isEmpty(hostName)) {
				LOG.info("Emailing is disabled. To enable, configure the mail server host name in db");
			} else {
				mailSender.setHost(hostName);
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper mime = new MimeMessageHelper(message, true, MapsConstants.CHARACTER_ENCODING);
				if (emailBean.isDefaultFromRequired()) {
					SessionBean sessionBean = (SessionBean) CustomApplicationContext.getApplicationContext().getBean("sessionBean");
					emailBean.setFrom(sessionBean.getInstName());
				}
				String[] fromMail=emailBean.getFrom().split(",");
				mime.setFrom(fromMail[0]);
				mime.setTo(emailBean.getTo().split(","));
				if (!nullOrEmpty(emailBean.getCc())) {
					mime.setCc(emailBean.getCc().split(","));
				}
				mime.setPriority(getPriority());
				mime.setSubject(emailBean.getSubject());
				if(emailBean.getVelocityTemplateName()!=null){
					//text = VelocityEngineUtils.mergeTemplateIntoString(getFileResourceLoaderEngine(), emailBean.getVelocityTemplateName(), MapsConstants.CHARACTER_ENCODING, emailBean.getBodyVariables());
					velocityEngine = getFileResourceLoaderEngine();
					velocityEngine.init();		
			        VelocityContext context = new VelocityContext(emailBean.getBodyVariables());
			        Template template = velocityEngine.getTemplate(emailBean.getVelocityTemplateName());    
			        StringWriter writer = new StringWriter();
			        template.merge(context, writer);
			        text = writer.toString();
				}else{
					text=emailBean.getText();
				}
				//String text="Testing for forgotpassword";
				
				if (!emailBean.getAttachFilePath().equalsIgnoreCase("")) 
				{
					if(validator.validatePath(emailBean.getAttachFilePath())){ 
		        File poFile=new File(emailBean.getAttachFilePath());
		        if(poFile.exists())
		        {
		        	 MimeBodyPart mbp1 = new MimeBodyPart();
		        	 mbp1.setText(text, "Cp1252", "html");
		             mp.addBodyPart(mbp1);
		          // START Attach multiple files which should be comma separated                         				
		             aryAttachFilePath = emailBean.getAttachFilePath().split(",");
		             MimeBodyPart mbp[] = new MimeBodyPart[aryAttachFilePath.length];
		         
		             for(int i=0;i<aryAttachFilePath.length;i++)
		             {  
		            	 try
		            	 {
		            		 mbp[i] = new MimeBodyPart();
		            		 DataSource source = new FileDataSource(aryAttachFilePath[i]);
		            		 mbp[i].setDataHandler(new DataHandler(source));
		            		 String psFileName = aryAttachFilePath[i].substring(aryAttachFilePath[i].lastIndexOf("/"));
		            		 psFileName = psFileName.substring(1);
		            		 mbp[i].setFileName(psFileName); 
		            		 mp.addBodyPart(mbp[i]); 
		            	 }
		            	 catch(Exception e)
		            	 {
		            		// e.printStackTrace();
		            		 LOG.error(e);
		            	 }
		             }
		             // END Attach multiple files which should be comma separated				       
		             message.setContent(mp,"text/html");
		        	}
				}
			mailSender.send(message);
			}
			}
		}catch(Exception e){
		//	e.printStackTrace();
			LOG.error("Failed to send email", e);
			throw e; 
		}
	}
	//New SMTP Configuration -- Added By Dhileep C
	public void sendEmailAPI(EmailBean emailBean) throws Exception {
		EmailBean mailbean = new EmailBean();
        try {
        	LOG.logMethodEntry("Entering Send E-mail API method........");
            // 1. Build POJO request from EmailBean
          	EmailAPIRequest requestObj = buildEmailJsonRequest(emailBean);
            
            // 2. Convert POJO -> JSON
            ObjectMapper mapper = new ObjectMapper();
            String reqJsonPayload = mapper.writeValueAsString(requestObj);

            LOG.info("Email API Request JSON = " + reqJsonPayload);
            
            // 3. Call Email API
            EmailAPIResponse apiResponse = callEmailAPI(reqJsonPayload);
            
            LOG.info("Email API Response JSON = " + mapper.writeValueAsString(apiResponse));
            
            mailbean.setReqUnqRefNo(headersMap.get("requestUniqueRefNo"));
            mailbean.setReqTimeStamp(headersMap.get("requestTimestamp"));
            mailbean.setReqJson(reqJsonPayload);
            mailbean.setResJson(apiResponse != null ? mapper.writeValueAsString(apiResponse): null);
             
            // 4. Handle response: log, throw if necessary
            if (apiResponse != null && apiResponse.getAPIStatus() != null) {
                 mailbean.setStatusMsg(apiResponse.getAPIStatus().getStatusMessage());
            	 mailbean.setStatusType(apiResponse.getAPIStatus().getStatusType());
            	 mailbean.setStatusCode(apiResponse.getAPIStatus().getStatusCode());
            	 mailbean.setStatusDesc(apiResponse.getAPIStatus().getStatusDescription());


            	 if (apiResponse.getAPIStatus().getAppStatusDetails() != null
            	            && !apiResponse.getAPIStatus().getAppStatusDetails().isEmpty()) {

            	        mailbean.setAppStatusCode(apiResponse.getAPIStatus().getAppStatusDetails().get(0).getAppStatusCode());
            	    }
            	
            	 
            	 if(apiResponse.getAPIStatus() != null && "SUCCESS".equalsIgnoreCase(mailbean.getStatusMsg().trim())) {
            		 mailbean.setMailStus(1); //success flag
            		 LOG.info("API got successful response!!!");
            	 } else {
            		 mailbean.setMailStus(0);
            		 LOG.error("Email API returned non-success:"+mapper.writeValueAsString(apiResponse)); 
            	 }
            	//Status Logs
               /* LOG.info("Email API statusType: "+ mailbean.getStatusType());
                LOG.info("Email API statusCode: "+ mailbean.getStatusCode());
                LOG.info("Email API statusMessage : "+ mailbean.getStatusMsg());
                LOG.info("Email API statusDescription : "+ mailbean.getStatusDesc());
                LOG.info("Email API appStatusCode : "+mailbean.getAppStatusCode()); 
                
               if (statusMsg == null || !"SUCCESS".equalsIgnoreCase(statusMsg.trim())) {
                    LOG.error("Email API returned non-success:"+apiResponse.getAPIStatus());
                    mailbean.setMailStus(0);
                }*/
                
            } else {
                LOG.error("Email API returned empty or invalid response");
                mailbean.setMailStus(0);
                mailbean.setStatusMsg("NO RESPONSE");
           	 	mailbean.setStatusType("TECHNICAL");
           	 	mailbean.setStatusCode("API_ERROR");
           	 	mailbean.setStatusDesc("Email API returned null or exception occurred");
            }
            emailServiceDao.saveEmailAudit(mailbean);
            LOG.logMethodExit("Exiting the Send E-mail API");
        } catch (Exception e) {
            LOG.error("Failed to send email (via API)", e);
            mailbean.setMailStus(0);
            throw e;
        }
    }
	
	// Build final HTML using Velocity for template
	private String buildEmailHtml(EmailBean emailBean) throws Exception {
        if (emailBean.getVelocityTemplateName() != null) {
            VelocityEngine velocityEngine = getFileResourceLoaderEngine(); // reuse your existing method
            velocityEngine.init();

            VelocityContext context = new VelocityContext(emailBean.getBodyVariables());
            Template template = velocityEngine.getTemplate(emailBean.getVelocityTemplateName());
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } else {
            return emailBean.getText() != null ? emailBean.getText() : "";
        }
    }
	
	//Exact JSON structure - EMAIL API Request
	private EmailAPIRequest buildEmailJsonRequest(EmailBean emailBean) {

		try {
			
			 EmailAPIRequest request = new EmailAPIRequest();
			 
			 EmailAPIRequest.RequestData data = new EmailAPIRequest.RequestData();
			 data.setDestinationMode("EMAIL");

			 EmailAPIRequest.TemplateInformation templateInfo = new EmailAPIRequest.TemplateInformation();
			 List<EmailAPIRequest.FieldData> fieldList = new ArrayList<>();
			 
			 Map<String, Object> vars = (emailBean.getBodyVariables() == null)
			            ? Collections.emptyMap()
			            : emailBean.getBodyVariables();
			 
			
	        // Required fields for API
			    fieldList.add(field("TemplateType", getString(vars,"TemplateType", null)));
			    fieldList.add(field("CIF", getString(vars, "CIF", "1160177")));
			    fieldList.add(field("EmailID", emailBean.getTo()));
			    fieldList.add(field("MobileNumber", getString(vars, "MobileNumber", "971509482519")));
			    fieldList.add(field("Language", getString(vars, "Language", "EN")));
			    fieldList.add(field("File", filePath(vars)));
			    fieldList.add(field("User_ID", getString(vars, "user_id",null)));
			    fieldList.add(field("Mer_ID", merid(vars)));
			    fieldList.add(field("Mer_Name", getString(vars, "mer_name",null)));
			    fieldList.add(field("Inst_Name", getString(vars, "instName",null)));

	        // Add remaining fields from bodyVariables (avoid duplicates)
	        for (Map.Entry<String, Object> e : vars.entrySet()) {
	            if (!exists(fieldList, e.getKey())) {
	            	fieldList.add(field(e.getKey(), String.valueOf(e.getValue())));
	            }
	        }

	        templateInfo.setTemplateData(fieldList);
	        data.setTemplateInformation(templateInfo);
	        request.setData(data);

	        return request;

	    } catch (Exception e) {
	        LOG.error("Error building Email JSON request", e);
	        return null;
	    }
    }
	
	//Call Email API here
	private EmailAPIResponse callEmailAPI(String jsonPayload) {
		  EmailAPIResponse responseObj = new EmailAPIResponse();

		    try {
		     // Step 1: Build Header
	            headersMap = EmailAPIHeaderBuilder.buildHeader(jsonPayload);

	            // Step 2: Create HTTP headers
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            headersMap.forEach(headers::set);
	            
	            /*LOG.info("\n===== REQUEST HEADERS =====");
	            headers.forEach((key, value) -> {
	            	LOG.info(key + " : " + value);
	            });*/
	            // Step 3: Set Entities
		        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
		        
		        // Step 4: Make POST call
		        ResponseEntity<EmailAPIResponse> response = restTemplate.exchange(this.getMpsConstantsInstance().getPropValue("EMAIL_API_URL"), HttpMethod.POST, entity, EmailAPIResponse.class);
		        responseObj = response.getBody();

		    } catch (Exception e) {
		        LOG.error("Error calling Email API", e);
		    }

		    return responseObj;
    }
	
	private EmailAPIRequest.FieldData field(String name, String value) {	
	    EmailAPIRequest.FieldData f = new EmailAPIRequest.FieldData(name, value);
	    f.setFieldName(name);
	    f.setFieldValue(value);
	    return f;
	}


	private boolean exists(List<EmailAPIRequest.FieldData> list, String key) {
	    for (EmailAPIRequest.FieldData f : list) {
	        if (f.getFieldName().equalsIgnoreCase(key)) return true;
	    }
	    return false;
	}

/*	private String getString(Map<String, Object> map, String key) {
		if (map == null || map.get(key) == null) return null;
	    return String.valueOf(map.get(key));
	} */

	private String getString(Map<String, Object> map, String key, String defaultValue) {
		if (map == null || map.get(key) == null){
	        return defaultValue; 
	    }
	    return String.valueOf(map.get(key));
	}
	
	private String filePath(Map<String, Object> vars) {
	    String[] keys = {"file", "MakerPath"};
	    for (String key : keys) {
	        if (vars.containsKey(key)) {
	            return String.valueOf(vars.get(key));
	        }
	    }
	    return null;  // return null if no file path is provided
	}
	
	private String merid(Map<String, Object> vars) {
	    String[] keys = {"id", "mer_id"};
	    for (String key : keys) {
	        if (vars.containsKey(key)) {
	            return String.valueOf(vars.get(key));
	        }
	    }
	    return null;  // return null if no file path is provided
	}
	
	
}
