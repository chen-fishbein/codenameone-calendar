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

package com.codename1.calendar.impl;

import com.codename1.system.NativeInterface;

/**
 * Interface to calendar functions for Android & iOS
 * 
 * @author Kapila de Lanerolle
 * @author Andreas Heydler
 */
public interface CalendarNativeInterface extends NativeInterface {
   
   /**
    * @return If we have permissions for calendar modifications
    */
   boolean hasPermissions();
   
   /**
    * Query device and return number of available calendars.
    * 
    * @return Number of available calendars
    * 
    */
   int getCalendarCount();
   
   /**
    * @param offset - offset into available calendar list 
    * @return - Name of the calendar at the offset. Null if there is no calendar at this offset.
    */
   String getCalendarName(int offset);

   /**
    * Opens the named calendar creating it if necessary.
    *
    * @param calendarName      - Name of calendar to be opened/created
    * @param createIfNotExists - Indicates if a calendar must be created if there is no calendar found by the name provided
    * @return Unique ID to be used in other methods referencing this calendar. Null in case of failure or calendar does not exist
    */
   String openCalendar(String calendarName, boolean createIfNotExists);

   /**
    * Add/Edit an event in named calendar
    *
    * @param calendarID     - As returned from openCalendar
    * @param eventID        - Event Identifier. Pass null for new Events
    * @param title          - Title of the Calendar Event
    * @param startTimeStamp - Event starting time stamp (unix time)
    * @param endTimeStamp   - Event ending time stamp (unix time)
    * @param allDayEvent    - The event is an all day event
    * @param taskOnly       - Task Only. No time associated with this.
    * @param notes          - Any notes for the event
    * @param location       - Location of the event
    * @param reminders      - alarm offsets (in seconds) in CSV format. Pass null for no alarms
    * @return Unique event identifier for the event that's created. Null in the case of failure
    */
   String saveEvent(String calendarID, String eventID, String title, long startTimeStamp, long endTimeStamp, boolean allDayEvent, boolean taskOnly, String notes, String location, String reminders);

   /**
    * Removes event with previously returned eventID
    *
    * @param calendarID	- As returned from openCalendar
    * @param eventID 	- As returned from saveEvent
    * @return If removal successful
    */
   boolean removeEvent(String calendarID, String eventID);

   /**
    * Query calendar and return details as XML string
    *
    * @param calendarID	- As returned from openCalendar
    * @param eventID    - As returned from saveEvent
    * @return XML String of event details. Null if not found.
    */
   String getEventByID(String calendarID, String eventID);

   /**
    * Returns all events in the calendar between startTimeStamp and endTimeStamp
    *
    * @param calendarID     - As returned from openCalendar
    * @param startTimeStamp - Event starting time stamp (unix time)
    * @param endTimeStamp   - Event ending time stamp (unix time)
    * @return XML string of all events found
    */
   String getEvents(String calendarID, long startTimeStamp, long endTimeStamp);

   /**
    * The following are for registering/deregistering for receiving callbacks when calendar events are modified
    */
   void registerForEventNotifications();

   void deregisterForEventNotifications();

}
