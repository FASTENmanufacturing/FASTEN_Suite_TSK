package com.fasten.wp4.database.serializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import com.fasten.wp4.database.model.DemandProjected;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDemandProjectedSerializer extends StdSerializer<List<DemandProjected>>{
		 
		private static final long serialVersionUID = 1L;

		public CustomDemandProjectedSerializer() {
		        this(null);
		    }
		 
		    public CustomDemandProjectedSerializer(Class<List<DemandProjected>> t) {
		        super(t);
		    }
		 
		    @Override
		    public void serialize(List<DemandProjected> demandProjecteds, JsonGenerator generator,SerializerProvider provider) throws IOException, JsonProcessingException {
		    	
		    	generator.writeStartArray();
			    	for (DemandProjected demandProjected : demandProjecteds) {
			    		
			    		generator.writeStartObject();
				            Field[] fields = demandProjected.getClass().getDeclaredFields();
				            for (Field field : fields) {
				            	field.setAccessible(true);
				            	if(field.getName().contentEquals("demandProjectionStudy")) {
				            		generator.writeObjectFieldStart("demandProjectionStudy");
					            		generator.writeObjectField("id",demandProjected.getDemandProjectionStudy().getId());
					            	generator.writeEndObject();
				            	}else {
				            		try {
				            			generator.writeObjectField(field.getName(), field.get(demandProjected));
				            		} catch (IllegalArgumentException | IllegalAccessException e) {
				            			e.printStackTrace();
				            		}
				            	}
				            }
			            generator.writeEndObject();
			    		
			        }
		    	generator.writeEndArray();
		    	
		    }
		    
}
