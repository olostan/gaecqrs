package com.olostan.gaecqrs.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EventInfo implements IsSerializable {
	
	private Date when;
	public EventInfo() { };
	
	public EventInfo(String info) {
		super();
		this.info = info;
		when = new Date();
	}

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	public Date getWhen() {
		return when;
	}
}
