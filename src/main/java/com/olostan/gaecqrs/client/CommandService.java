package com.olostan.gaecqrs.client;

import simplejcqrs.commands.Command;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("CommandService")
public interface CommandService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static CommandServiceAsync instance;
		public static CommandServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(CommandService.class);
			}
			return instance;
		}
	}

	String sendCommand(Command cmd);
}
