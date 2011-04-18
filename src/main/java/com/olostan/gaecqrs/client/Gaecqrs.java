package com.olostan.gaecqrs.client;


import java.util.StringTokenizer;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.olostan.gaecqrs.client.widgets.HouseWidget;
import com.olostan.gaecqrs.client.widgets.EventLogWidget;
import com.olostan.gaecqrs.shared.DepartmentInfo;
import com.olostan.gaecqrs.shared.EventInfo;
import com.olostan.gaecqrs.shared.clientevents.EventTypes;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gaecqrs implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private final QueryServiceAsync queryService = GWT
	.create(QueryService.class);

	
	final EventLogWidget log = new EventLogWidget();
	final HouseWidget deps = new HouseWidget();
	
	private String token;	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {		
		
		RootPanel.get("eventlog").add(log);		
		final Label l = new Label();
		l.setText("auth...");					
		RootPanel.get("content").add(l);
		RootPanel.get("loading").setVisible(false);
		log.addEvent(new EventInfo("Loaded"));
		greetingService.greetServer(String.valueOf(Math.random()),new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				l.setText("Server is unreachable");				
			}

			@Override
			public void onSuccess(String result) {
				token = result;
				l.setText("starting...");
				log.addEvent(new EventInfo("Authorized"));
				ChannelFactory.createChannel(token, new ChannelFactory.ChannelCreatedCallback() {					
					@Override
					public void onChannelCreated(Channel channel) {
						l.setText("showing departments");
						channel.open(new ChannelListener());
						loadDepartments();
						RootPanel.get("content").add(deps);
					}
				});
			}
		});
				
	}
	private void loadDepartments() {
		queryService.getDepartments(new AsyncCallback<DepartmentInfo[]>() {			
			@Override
			public void onSuccess(DepartmentInfo[] result) {
				log.addEvent("got dep list: "+result.length);
				for (DepartmentInfo i : result) {
					log.addEvent("dep key="+i.getKey()+", name="+i.getName());
					deps.UpdateDepartment(i);
				}
					
			}			
			@Override
			public void onFailure(Throwable caught) {
			}
		});		
	}
	private class ChannelListener implements SocketListener 
	{
		@Override
		public void onOpen() {
			log.addEvent("channel opened");			
		}

		@Override
		public void onMessage(String message) {
			
			String[] tokens = message.split("\\|");
			if (tokens.length==3) {
				String type = tokens[0];
				String msg = tokens[1];
				String data = tokens[2];
				log.addEvent("type:"+type+" msg:"+msg+" data:"+data);
			
				if (type.equals(EventTypes.DEPARTMENT_EVENT)) {
					deps.UpdateDepartment(msg,data);
				}
			} else 
				log.addEvent("unknown message:"+message+"("+tokens.length+")");
		}

		@Override
		public void onError(SocketError error) {
			log.addEvent("chennel error");			
		}

		@Override
		public void onClose() {
			log.addEvent("chennel closed");			
		}		
	}
}
