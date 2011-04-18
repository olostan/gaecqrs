package com.olostan.gaecqrs.client.widgets;

import java.util.LinkedList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.olostan.gaecqrs.shared.EventInfo;

public class EventLogWidget extends Composite 
{
	private static EventLogWidget instance;
	
	static class EventInfoCell extends AbstractCell<EventInfo> 
	{
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				EventInfo value, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<div><small>");
			sb.appendEscaped(value.getWhen().toString());
			sb.appendHtmlConstant("</small>:<b>");			
			sb.appendEscaped(value.getInfo());
			sb.appendHtmlConstant("</b></div>");			
		}
		
	}
	
	private CellList<EventInfo> cellList;
	ScrollPanel panel = new ScrollPanel();
	
	public EventLogWidget() 
	{		
		// Create a CellList.
		EventInfoCell contactCell = new EventInfoCell();
		cellList = new CellList<EventInfo>(contactCell);
		cellList.setPageSize(10);
		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		
		panel.add(cellList);
		panel.setStyleName("scrollable");
		initWidget(panel);
				
		updater = new Timer() {
			@Override
			public void run() {
				panel.scrollToBottom();				
			}
			
		};
		instance = this;
	}
	Timer updater;
	LinkedList<EventInfo> data = new LinkedList<EventInfo>();

	
	public void addEvent(EventInfo info) {
		data.add(info);				
		cellList.setRowCount(data.size());
		cellList.setRowData(data);
		updater.schedule(100);
	}


	public void addEvent(String string) {
		addEvent(new EventInfo(string));		
	}


	public static EventLogWidget getInstance() {
		return instance;
	}

}
