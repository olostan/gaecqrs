package com.olostan.gaecqrs.server;

import simplejcqrs.commands.Command;

import com.olostan.gaecqrs.client.CommandService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CommandServiceImpl extends RemoteServiceServlet implements CommandService {

	@Override
	public String sendCommand(Command cmd) {
		return "ok";
	}
}
