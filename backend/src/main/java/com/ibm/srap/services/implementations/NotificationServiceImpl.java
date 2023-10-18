package com.ibm.srap.services.implementations;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.srap.client_beans.NotificationDO;
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.services.NotificationService;

@Service("NotificationService")
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class.getName());
	

	@Override
	public OperationResult sendNotification(NotificationDO notification) {
		String vCap = System.getenv("VCAP_SERVICES");
		if (vCap == null) return new OperationResult(false, "BlueMail service not found");
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode mainNode;
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			mainNode = mapper.readValue(vCap, ObjectNode.class);
			JsonNode mailNode = mainNode.get("bluemailservice");
			ObjectNode credentialsNode = (ObjectNode) mailNode.get(0);
			JsonNode configNode = credentialsNode.get("credentials");
			
			String url = configNode.get("emailUrl").asText();
			String user = configNode.get("username").asText();
			String pass = configNode.get("password").asText();
			
			HttpPost httpPost = new HttpPost(url);
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, pass);
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
			
			ObjectNode main = mapper.createObjectNode();
			main.put("contact", notification.getContact());
			
			ArrayNode contactArray = mapper.createArrayNode();
			for (String rec : notification.getRecipients()) {
				ObjectNode recipient = mapper.createObjectNode();
				recipient.put("recipient", rec);
				contactArray.add(recipient);
			}
			
			main.set("recipients", contactArray);
			main.put("subject", notification.getSubject());
			main.put("message", notification.getMessage());
			String json = mapper.writeValueAsString(main);
			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Content-Type", "application/json");
			
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "Notification response status: {0}", status);
			
			if (status != 201) return new OperationResult(false, "Notification response code " + status);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
			return new OperationResult(false, "Problems encountered trying to send notification");
		} catch (AuthenticationException e) {
			logger.log(Level.SEVERE, e.getMessage());
			return new OperationResult(false, "Not authorized for BlueMail service");
		} 
		
		return new OperationResult(true, "OK");
	}
	
}
