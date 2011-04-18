package com.olostan.gaecqrs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import simplejcqrs.commands.Command;

public interface CommandServiceAsync {
	void sendCommand(Command cmd,  AsyncCallback<String> callback)
		throws IllegalArgumentException;
}
