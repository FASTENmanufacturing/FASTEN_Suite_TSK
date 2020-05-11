package com.fasten.wp4.infra.converter;

import java.lang.reflect.Field;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "entityApiConverter", managed=true)
//from https://www.guj.com.br/t/para-voce-entityconverter-para-qualquer-entidade-e-tipo-de-id/271033
public class EntityApiConverter implements Converter<Object> {

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {
			return component.getAttributes().get(value);
		}
		return null;
	}

	public String getAsString(FacesContext ctx, UIComponent component, Object obj) {
		if (obj != null && !"".equals(obj)) {
			String id;

			id = this.getId(getClazz(ctx, component), obj);
			if (id == null){
				id = "";
			}
			id = id.trim();
			component.getAttributes().put(id, getClazz(ctx, component).cast(obj));
			return id;
		}
		return null;
	}

	private Class<?> getClazz(FacesContext facesContext, UIComponent component) {
		return component.getValueExpression("value").getType(facesContext.getELContext());
	}

	public String getId(Class<?> clazz, Object obj){
		try {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getName().contentEquals("id")) {
					Field privateField = clazz.getDeclaredField(field.getName());
					privateField.setAccessible(true);
					if (privateField.get(clazz.cast(obj)) != null) {
						return (String)field.getType().cast(privateField.get(clazz.cast(obj))).toString();
					} else {
						return null;
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace(); 
		} catch (IllegalArgumentException e) {
			e.printStackTrace(); 
		} catch (NoSuchFieldException e) {
			e.printStackTrace(); 
		} catch (IllegalAccessException e) {
			e.printStackTrace(); 
		}
		return null;
	}
}