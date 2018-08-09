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

      id = element.getFirstChildByTagName("id").getChildAt(0).getText();
      
      Element titleE = element.getFirstChildByTagName("title");
      if(titleE.isEmpty()) {
          title = "";
      } else {
          title = titleE.getChildAt(0).getText();
      }
      
      Element descriptionE = element.getFirstChildByTagName("description");
      if(descriptionE.isEmpty()) {
        description = "";  
      } else {
        description = descriptionE.getChildAt(0).getText();
      }
      
      Element locationE = element.getFirstChildByTagName("location");
      if(locationE.isEmpty()) {
          location = "";
      } else {
          location = locationE.getChildAt(0).getText();
      }
      
      Element startTimeE = element.getFirstChildByTagName("startTimeStamp");
      if(startTimeE.isEmpty()) {
          startTime = null;
      } else {
          startTime  = new Date(Long.parseLong(startTimeE.getChildAt(0).getText()));
      }
      
      Element endTimeE = element.getFirstChildByTagName("endTimeStamp");
      if(endTimeE.isEmpty()) {
          endTime = null;
      } else {
          endTime = new Date(Long.parseLong(endTimeE.getChildAt(0).getText()));
      }
      
      Element allDayEventE = element.getFirstChildByTagName("allDayEvent");
      if(allDayEventE.isEmpty()) {
          allDayEvent = false;
      } else {
          allDayEvent    = "true".equals (allDayEventE.getChildAt(0).getText());
      }

      Element rems = element.getFirstChildByTagName("reminders");

      if (rems != null && rems.getNumChildren() > 0) {
         Vector<Element> seconds = rems.getChildrenByTagName("reminderOffset");

         reminders = new int[seconds != null ? seconds.size() : 0];

         for (int i = 0; i < reminders.length; i++)  {
            reminders[i] = Integer.parseInt(seconds.elementAt(i).getChildAt(0).getText());            
         }
      }
      else {
         reminders = new int[0];
      }
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
