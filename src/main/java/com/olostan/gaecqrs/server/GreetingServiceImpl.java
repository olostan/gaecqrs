package com.olostan.gaecqrs.server;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.olostan.gaecqrs.client.GreetingService;

/**
 * The server side implementation of the RPC service.
 */

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	
	private final LinkedList<String> users = new LinkedList<String>();
	private final HashMap<String,Date> lastMessage = new HashMap<String,Date>();
	
	public static GreetingServiceImpl instance = null;	
		
	@Override
	public void init() throws ServletException {		
		super.init();
		instance = this;
	}	
	
	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}


	public String greetServer(String input) throws IllegalArgumentException {

		input = escapeHtml(input);
	    //String agent = getThreadLocalRequest().getHeader("User-Agent");
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		// temp
		//input = agent;
		if (input.length()>60) input = input.substring(0,60);
		String token = channelService.createChannel(input);
		LoginUser(input);		
		RemoveOldUsers();
		NotifyAllClients("Connected "+input);
		
		//log.info("Connected "+input+" token="+token);
		return token;
	}
	private void LoginUser(String user) {
		boolean newUSer = true;
		if (users.contains(user)) {
			newUSer = false;
			users.remove(user);
			ChannelService channelService = ChannelServiceFactory.getChannelService();			
			channelService.sendMessage(new ChannelMessage(user, "relog"));			
		}			
		users.add(user);
		Refresh(user);
		if (newUSer) {
			//Key nKey =  KeyFactory.createKey("department", user);
			//EventBus.getBus().publish(new DepartmentAdded("Department "+user, nKey));
		}
		
	}
	private void Refresh(String user) {
		lastMessage.put(user, new Date());
	}
	private void RemoveOldUsers() {
		Calendar calendar = Calendar.getInstance();		
		calendar.add(Calendar.MINUTE, -30);
		//calendar.add(Calendar.SECOND, -20);
		Date ago = calendar.getTime();
		LinkedList<String> toDelete = new LinkedList<String>();
				
		for(String user : users) {
			Date last = lastMessage.get(user);
			if (last==null || last.before(ago))
			{
				lastMessage.remove(user);
				toDelete.add(user);
				System.out.println("Removing "+user);
			}
		}
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		for(String user : toDelete) {
			users.remove(user);
			channelService.sendMessage(new ChannelMessage(user, "timeout"));
		}
	}

	
	private void NotifyAllClients(String string) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		System.out.println("starting sending");
		for(String token : users) {
			channelService.sendMessage(new ChannelMessage(token, string));
			System.out.println("Sending message to "+token+": "+string);
		}
		System.out.println("finished");		
	}
	
	public void SendToAll(String type, String msg, String data) {
		NotifyAllClients(type+"|"+msg+"|"+data);
	}


	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
