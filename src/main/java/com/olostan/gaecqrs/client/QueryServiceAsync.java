package com.olostan.gaecqrs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.olostan.gaecqrs.shared.DepartmentInfo;

public interface QueryServiceAsync {
	void getDepartments(AsyncCallback<DepartmentInfo[]> callback)
	throws IllegalArgumentException;
}
