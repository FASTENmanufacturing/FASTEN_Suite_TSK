package com.github.adminfaces.starter.infra.converter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("prettyTimeConverter")
public class PrettyTimeConverter implements Converter {
	  
	  public String getAsString(FacesContext context, UIComponent component,
	      Object value) {
	    if (value == null) {
	      return null;
	    }
	  
	    return convertToDaysHoursMinutes(Long.valueOf( ((int)value)));
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
		    Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		    
		    if (day != 0){
		        result += day;
		        if (day == 1){
		        	if (locale == Locale.ENGLISH) {
		        		result += " day ";
		        	}else {
		        		result += " dia ";
		        	}
		        }
		        else{
		        	if (locale == Locale.ENGLISH) {
		        		result += " days ";
		        	}else {
		        		result += " dias ";
		        	}
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
		        result += minute + " min";;
		    }
		    
		    if (seg != 0){
		        result += " " + seg + " s";
		    }

		    return result;
		}

}
