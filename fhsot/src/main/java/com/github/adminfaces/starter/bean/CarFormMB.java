/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import com.github.adminfaces.starter.model.Car;
import com.github.adminfaces.starter.service.CarService;

/**
 * @author rmpestano
 */
@Named
@ViewScoped
public class CarFormMB implements Serializable {


    private Integer id;
    private Car car;


    @Inject
    CarService carService;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            car = carService.findById(id);
        } else {
            car = new Car();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    public void remove() throws IOException {
        if (has(car) && has(car.getId())) {
            carService.remove(car);
            addDetailMessage("Car " + car.getModel()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("car-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (car.getId() == null) {
            carService.insert(car);
            msg = "Car " + car.getModel() + " created successfully";
        } else {
            carService.update(car);
            msg = "Car " + car.getModel() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        car = new Car();
        id = null;
    }

    public boolean isNew() {
        return car == null || car.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String entityName = admin.getString("car");
    	String newExpression = admin.getString("breadcum.new");
    	if(car.getId() == null) {
    		return MessageFormat.format(newExpression, entityName);
    	}else {
    		return StringUtils.capitalize(entityName).concat(" "+car.getId());
    	}
    }

}
