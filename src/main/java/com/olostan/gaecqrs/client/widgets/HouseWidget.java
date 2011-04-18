package com.olostan.gaecqrs.client.widgets;

import java.util.LinkedList;
import java.util.List;

import simplejcqrs.commands.HouseCommands;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.olostan.gaecqrs.client.CommandService;
import com.olostan.gaecqrs.client.CommandServiceAsync;
import com.olostan.gaecqrs.client.QueryService;
import com.olostan.gaecqrs.client.QueryServiceAsync;
import com.olostan.gaecqrs.shared.DepartmentInfo;

public class HouseWidget extends Composite 
{
			
	List<DepartmentInfo> list = new LinkedList<DepartmentInfo>();
	private CellList<DepartmentInfo> cellList;
	
	private final CommandServiceAsync commandService = GWT.create(CommandService.class);
	
	public HouseWidget() 
	{
		ProvidesKey<DepartmentInfo> keyProvider = new ProvidesKey<DepartmentInfo>() {
		      public Object getKey(DepartmentInfo item) {
		        return (item == null) ? null : item.getKey();
		      }
		    };
	    AbstractCell<DepartmentInfo> depCell = new AbstractCell<DepartmentInfo>() 
		{
				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context,
						DepartmentInfo value, SafeHtmlBuilder sb) {
					sb.appendHtmlConstant("<div>D:<b>");
					sb.appendEscaped(value.getName());
					sb.appendHtmlConstant("</b>");			
					sb.appendHtmlConstant("</div>");			
				}				
		};
		cellList = new CellList<DepartmentInfo>(depCell, keyProvider);
		cellList.setPageSize(10);
		VerticalPanel vpanel = new VerticalPanel();
		
		Button addBtn = new Button("Add");
		vpanel.add(addBtn);
		addBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				commandService.sendCommand(new HouseCommands.CreateHouse("someid", "some address"), new AsyncCallback<String>() {					
					@Override
					public void onSuccess(String result) {
						EventLogWidget.getInstance().addEvent("Command sent:"+result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						EventLogWidget.getInstance().addEvent("Command failed:"+caught.toString());
					}
				});
			}
		});
		
		ScrollPanel panel = new ScrollPanel();		
		panel.add(cellList);
		panel.setStyleName("scrollable");
		vpanel.setWidth("100%");
		vpanel.add(panel);
		initWidget(vpanel);
	}
	public void UpdateDepartment(DepartmentInfo info) {
		int i = list.indexOf(info);
		if (i==-1) {
			list.add(info);
		} else 
		{			
			list.set(i, info);
		}
		cellList.setRowCount(list.size());
		cellList.setRowData(list);
	}
	public void UpdateDepartment(String msg, String data) {
		DepartmentInfo info = DepartmentInfo.fromString(data);
		UpdateDepartment(info);
	}

}
