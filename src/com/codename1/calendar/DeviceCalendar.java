/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Codename One through http://www.codenameone.com/ if you 
 * need additional information or have any questions.
 */

package com.codename1.calendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;

import com.codename1.calendar.impl.CalendarNativeInterface;
import com.codename1.io.CharArrayReader;
import com.codename1.system.NativeLookup;
import com.codename1.xml.Element;
import com.codename1.xml.XMLParser;

/**
 * This class will represent the user visible portable API
 *
 * @author Shai Almog
 * @author Kapila de Lanerolle
 * @author Andreas Heydler
 */
public final class DeviceCalendar {

	//Make DeviceCalendar a singleton
	private static DeviceCalendar INSTANCE = null;

	//Native Interface Implementation
	private CalendarNativeInterface impl = null;

	/**
	 * DeviceCalendar can be use to manipulate device calendars.
	 * @return	Instance of a DeviceCalendar implementation
	 */
	public static synchronized DeviceCalendar getInstance() {
		//We need to limit instantiation to the supported platforms.
		if(null == INSTANCE){
			//Let's see if we have an implementation for the platform
			CalendarNativeInterface impl = (CalendarNativeInterface) NativeLookup.create(CalendarNativeInterface.class);
			if(null != impl && impl.isSupported()){
				INSTANCE = new DeviceCalendar(impl);
			}
		}
		return INSTANCE;
	}
	
	/**
	 * @param impl	- Instance of NativeCalendarInterface implementation
	 */
	private DeviceCalendar(CalendarNativeInterface impl) {
		if(null == impl){
			//Shouldn't happen.
			throw new IllegalArgumentException("NativeInterface is null!");
		}
		this.impl = impl;
	}
	
	/**
	 * See if calendar interface available
	 * 
	 * @return true if calendar interface available & have permission to access
	 */
	public boolean hasPermissions() {
		return impl.hasPermissions();
	}

	/**
	 * @return array of Strings of calendar names or null if no permissions
	 */
	public Collection<String> getCalendars() {
		//Check if we have any calendars we can work with. If so, return a collection of their names.
		int cnt = impl.getCalendarCount();
		if(0 < cnt){
			Collection<String> list = new LinkedList<String>();

			for (int i = 0; i < cnt; i++)
				list.add(impl.getCalendarName(i));

			return list;         
		}

		return null;
	}

	/**
	 * Opens the named calendar creating it if necessary.
	 *
	 * @param calendarName      - Name of calendar to be opened/created. Pass null for default calendar on the device
	 * @param createIfNotExists - Indicates if a calendar must be created if there is no calendar found by the name provided
	 *                          
	 * @return Unique ID to be used in other methods referencing this calendar. Null in case of failure or calendar does not exist
	 */
	public String openCalendar(String calendarName, boolean createIfNotExists) {
		return impl.openCalendar(calendarName, createIfNotExists);
	}

	/**
	 * Add/Edit an event in named calendar.
	 *
	 * @param calendarID     - As returned from openCalendar
	 * @param eventID        - Event Identifier. Pass null for new Events
	 * @param title          - Title of the Calendar Event
	 * @param startTimeStamp - Event starting time stamp
	 * @param endTimeStamp   - Event ending time stamp
	 * @param allDayEvent    - The event is an all day event
	 * @param notes          - Any notes for the event
	 * @param location       - Location of the event
	 * @param reminders      - alarm offsets (in seconds). Pass null for no alarms
	 *                       
	 * @return Unique event identifier for the event that's created. Null in the case of failure or no permissions
	 */
	public String saveEvent(String calendarID, 
								   String eventID, 
								   String title,
								   Date startTimeStamp,
								   Date endTimeStamp,
								   boolean allDayEvent,
								   String notes,
								   String location,
								   Collection<Integer> reminders) {		
		if (calendarID == null || calendarID.length() == 0)
			throw new IllegalArgumentException("calendarID required");

		String sReminders = null;
      
		if (reminders != null && reminders.size() > 0) {
			sReminders = "";

			for (int reminder : reminders)
				sReminders += reminder + ",";

			sReminders = sReminders.substring(0, sReminders.length() - 1);
		}

		return impl.saveEvent(calendarID, eventID, title, startTimeStamp.getTime(), endTimeStamp.getTime(), allDayEvent, false, notes, location, sReminders);
	}

	/**
	 * Removes event with previously returned eventID
	 *
	 * @param calendarID	- As returned from openCalendar
	 * @param eventID 	- As returned from saveEvent
	 *                   
	 * @return If removal successful
	 */   
	public boolean removeEvent(String calendarID, String eventID) {
		if (calendarID == null || calendarID.length() == 0)
			throw new IllegalArgumentException("calendarID required");

		if (eventID == null || eventID.length() == 0)
			throw new IllegalArgumentException("eventID required");

		return impl.removeEvent(calendarID, eventID);
	}

	/**
	 * Query calendar and return details as an EventInfo
	 *
	 * @param calendarID	- As returned from openCalendar
	 * @param eventID    - As returned from saveEvent
	 *                   
	 * @return an EventInfo or null on failure, not found or no permissions.
	 */
	public EventInfo getEventByID(String calendarID, String eventID) {
		if (calendarID == null || calendarID.length() == 0)
			throw new IllegalArgumentException("calendarID required");

		if (eventID == null || eventID.length() == 0)
			throw new IllegalArgumentException("eventID required");

		String xml = impl.getEventByID(calendarID, eventID);
      
		if(null != xml){
			Element element = new XMLParser().parse(new CharArrayReader(xml.toCharArray()));
//         Log.p("event XML " + xml);
//         Log.p("parsed " + element);
			return new EventInfo(findElement(element, "response", "event"));         
		}
      
		return null;
	}

	/**
	 * Returns all events in the calendar between startTimeStamp and endTimeStamp
	 *
	 * @param calendarID     - As returned from openCalendar
	 * @param startTimeStamp - Event search starting time stamp                       
	 * @param endTimeStamp   - Event search ending time stamp
	 *                       
	 * @return collection of EventInfo's. Returns null in case of failure or no permissions
	 */
	public Collection<EventInfo> getEvents(String calendarID, Date startTimeStamp, Date endTimeStamp) {
		if (calendarID == null || calendarID.length() == 0)
			throw new IllegalArgumentException("calendarID required");

		String xml = impl.getEvents(calendarID, startTimeStamp.getTime(), endTimeStamp.getTime());
      
		if(null != xml){
			Element element = new XMLParser().parse(new CharArrayReader(xml.toCharArray()));

//         Log.p("events XML " + xml);
//         Log.p("parsed " + element);

			element = findElement(element, "response", "eventList"); 
			Collection<EventInfo> col = new ArrayList<EventInfo>();         

			if (element != null) {
				Vector<Element> events = element.getChildrenByTagName("event");

				if (events != null) {
					Enumeration<Element> enumeration = events.elements();

					while (enumeration.hasMoreElements())
						col.add(new EventInfo(enumeration.nextElement()));
				}
			}

			return col;         
		}

		return null;
	}
   
   private static Element findElement(Element element, String... tags) {
      if (element != null) {
         for (String tag : tags) {
            element = element.getFirstChildByTagName(tag);
            
//            Log.p("child " + element);
            
            if (element == null)
               break;            
         }
      }
      
      return element;
   }

//   public void registerForEventNotifications() {
//      if (hasPermissions())
//         ((CalendarNativeInterface) NativeLookup.create(CalendarNativeInterface.class)).registerForEventNotifications();
//   }
//
//   public void deregisterForEventNotifications() {
//      if (hasPermissions())
//         ((CalendarNativeInterface) NativeLookup.create(CalendarNativeInterface.class)).deregisterForEventNotifications();
//   }


}
