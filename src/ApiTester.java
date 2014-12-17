//CODE ON THIS PAGE WAS SUPPLEMENTED BY http://stackoverflow.com/a/2793153

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;


public class ApiTester 
{

	/**
	 * Controls the User I/O and calls the GET/POST functions
	 * 
	 * @param args not used
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		String url = "http://worldchat263.appspot.com/interact";
		String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter POST to post a message, GET to get chat history, or QUIT to quit.\n >");
		String userInput=reader.readLine();
		while(!userInput.equalsIgnoreCase("quit"))
		{
			if(userInput.equalsIgnoreCase("post"))
			{
				System.out.println("Enter message to post.\n >");
				String message = reader.readLine();
				Gson gson = new Gson();
				String jsonMessage=gson.toJson(message);
				String query = String.format("message=%s", URLEncoder.encode(jsonMessage, charset));
				postRequest(url, charset, query);
				
			}
			else if(userInput.equalsIgnoreCase("get"))
			{
				String hist=getRequest(url);
				System.out.println(hist);
			}
			System.out.print("Enter POST to post a message, GET to get chat history, or QUIT to quit.\n >");
			userInput=reader.readLine();	
		}
	}
	
	/**
	 * Does the HTTP GET Request, puts the response status in System.err
	 * 
	 * @param url URL of the get request
	 * @return ChatHistory String received from the get request
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String getRequest(String url) throws MalformedURLException, IOException
	{
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		//connection.setRequestProperty("Accept-Charset", charset);
		InputStream response = connection.getInputStream();
		String json = IOUtils.toString(response, "UTF-8");
		Gson gson = new Gson();
		String chatHistory=gson.fromJson(json, String.class);
		System.err.println("HTTP Response Status "+connection.getResponseCode());
		connection.disconnect();
		return chatHistory;
	}
	
	/**
	 * Does the HTTP POST Request, puts the response status in System.err
	 * 
	 * @param url URL of the get request
	 * @param charset charset of the message being sent
	 * @param message a message to send to the site
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void postRequest(String url, String charset, String message) throws MalformedURLException, IOException
	{
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

		try (OutputStream output = connection.getOutputStream()) {
		    output.write(message.getBytes(charset));
		}
		System.err.println("HTTP Response Status "+connection.getResponseCode());
		
		InputStream response = connection.getInputStream();
		connection.disconnect();
	}
}
