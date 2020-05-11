package com.fasten.wp4.infra.converter;

import java.util.concurrent.TimeUnit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("prettyHourConverter")
public class PrettyHourConverter implements Converter {
	  
	  public String getAsString(FacesContext context, UIComponent component,
	      Object value) {
	    if (value == null) {
	      return null;
	    }
	    
	    return convertToDaysHoursMinutes(Long.valueOf( Math.round((double)value *60*60)));
	  }
	  
	  public Object getAsObject(FacesContext context, UIComponent component,
	      String value) {
	    if (value == null) {
	      return null;
	    }
		return value;
	  
	  }
	  
	  
	  public static String convertToDaysHoursMinutes(long seconds) {

		    int day = (int)TimeUnit.SECONDS.toDays(seconds);
		    long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
		    long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60); 
		    long seg = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds)* 60);

		    String result = "";

		    if (day != 0){
		        result += day;
		        if (day == 1){
		            result += " day ";
		        }
		        else{
		            result += " days ";
		        }
		    }

		    if (hours != 0){
		        result += hours;

		        if (hours == 1){
		            result += " hr ";
		        }
		        else{
		            result += " hrs ";
		        }
		    }

		    if (minute != 0){
		        result += minute;

		        if (minute == 1){
		            result += " min";
		        }
		        else{
		            result += " mins";
		        }
		    }
		    
		    if (seg != 0){
		        result += " " + seg + " s";
		    }

		    return result;
		}

}
