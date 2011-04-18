package com.olostan.gaecqrs.server;

import javax.servlet.ServletException;

import com.olostan.gaecqrs.client.QueryService;
import com.olostan.gaecqrs.shared.DepartmentInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class QueryServiceImpl extends RemoteServiceServlet implements QueryService {
	
	
	@Override
	public void init() throws ServletException {		
		super.init();
		//EventBus.getBus().subscribe(list);
		//EventBus.getBus().RestoreInitialState();
	}

	@Override
	public DepartmentInfo[] getDepartments() {
		//return list.getDepartmentList();
		return new DepartmentInfo[0];
	}
	
}
