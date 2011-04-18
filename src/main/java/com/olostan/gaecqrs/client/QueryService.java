package com.olostan.gaecqrs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.olostan.gaecqrs.shared.DepartmentInfo;

@RemoteServiceRelativePath("QueryService")
public interface QueryService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static QueryServiceAsync instance;
		public static QueryServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(QueryService.class);
			}
			return instance;
		}
	}

	DepartmentInfo[] getDepartments();	
}
