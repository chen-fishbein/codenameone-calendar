/*
 * @author  Andreas
 * @version $Revision$
 *
 * Date: 10/02/14
 * Time: 4:48 PM
 * 
 * $Id$
 */
package com.codename1.calendar;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import com.codename1.xml.Element;

/**
*
*/
public class EventInfo {
   private final String  id;    
   private final String  title;    
   private final String  description;    
   private final String  location;
   private final Date    startTime;
   private final Date    endTime;
   private final boolean allDayEvent;
   private final int[]   reminders;

   public EventInfo(Element element) {
//      Log.p("EventInfo(" + element + ")");

      if (element == null)
         throw new IllegalArgumentException("element cannot be null");

      id             = element.getFirstChildByTagName("id").         getChildAt(0).getText();
      title          = element.getFirstChildByTagName("title").      getChildAt(0).getText();
      description    = element.getFirstChildByTagName("description").getChildAt(0).getText();
      location       = element.getFirstChildByTagName("location").   getChildAt(0).getText();
      startTime      = new Date(Long.parseLong(element.getFirstChildByTagName("startTimeStamp").getChildAt(0).getText()));
      endTime        = new Date(Long.parseLong(element.getFirstChildByTagName("endTimeStamp").  getChildAt(0).getText()));
      allDayEvent    = "true".equals (element.getFirstChildByTagName("allDayEvent").   getChildAt(0).getText());

      Element rems = element.getFirstChildByTagName("reminders");

      if (rems != null && rems.getNumChildren() > 0) {
         Vector<Element> seconds = rems.getChildrenByTagName("reminderOffset");

         reminders = new int[seconds != null ? seconds.size() : 0];

         for (int i = 0; i < reminders.length; i++) 
            reminders[i] = Integer.parseInt(seconds.elementAt(i).getChildAt(0).getText());            
      }
      else
         reminders = new int[0];
   }

   public String getID() {
      return id;
   }

   public String getTitle() {
      return title;
   }

   public String getDescription() {
      return description;
   }

   public String getLocation() {
      return location;
   }

   public Date getStartTime() {
      return startTime;
   }

   public Date getEndTime() {
      return endTime;
   }

   public boolean isAllDayEvent() {
      return allDayEvent;
   }

   public int[] getReminders() {
      return reminders;
   }

   @Override
   public String toString() {
      return "EventInfo{" +
              "id='"          + id          + '\'' +
            ", title='"       + title       + '\'' +
            ", description='" + description + '\'' +
            ", location='"    + location    + '\'' +
            ", startTime="    + startTime   +
            ", endTime="      + endTime     +
            ", allDayEvent="  + allDayEvent +
            ", reminders="    + Arrays.toString(reminders) +
            '}';
   }
}
