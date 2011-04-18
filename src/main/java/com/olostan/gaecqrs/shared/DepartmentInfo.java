package com.olostan.gaecqrs.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DepartmentInfo implements IsSerializable
{
	private String key;
	private String Name;	
	public DepartmentInfo(String key, String name) {
		super();
		this.key = key;
		Name = name;
	}
	public DepartmentInfo() {
		super();
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartmentInfo other = (DepartmentInfo) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	public static DepartmentInfo fromString(String data) {
		int sep = data.indexOf("&");
		String key = data.substring(0,sep);
		String name = data.substring(sep+1, data.length());
		DepartmentInfo info = new DepartmentInfo();
		info.key = key;
		info.Name = name;
		return info;
	}
	@Override
	public String toString() {
		return key + "&" + Name;
	}	
	
}