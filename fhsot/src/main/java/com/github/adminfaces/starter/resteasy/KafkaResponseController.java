package com.github.adminfaces.starter.resteasy;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasten.wp4.database.client.invoker.ApiException;
import com.github.adminfaces.starter.bean.AsyncNotifyMB;

@Path("kafka")
public class KafkaResponseController {

    @Inject
	AsyncNotifyMB asyncNotifyMB;

    @Path("/{topicName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPars(@PathParam("topicName") String topicName) throws ApiException {
    	asyncNotifyMB.sendPushMessage(topicName);
        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

//    @Path("/{id}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response findCity(@PathParam("id") Long id) {
//
//        City city = cityService.find(id);
//
//        if (city.getId() != null) {
//            return Response.ok(city).build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }
//
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public Response saveCity(@FormParam("name") String name,
//            @FormParam("population") int population) {
//
//        City city = new City();
//        city.setName(name);
//        city.setPopulation(population);
//
//        boolean r = cityService.save(city);
//
//        if (r) {
//            return Response.ok().status(Response.Status.CREATED).build();
//        } else {
//            return Response.notModified().build();
//        }        
//    }
//
//    @Path("/{id}")
//    @PUT
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public Response updateCity(@FormParam("name") String name,
//            @FormParam("population") int population,
//            @PathParam("id") Long id) {
//
//        City city = new City();
//        city.setName(name);
//        city.setPopulation(population);
//
//        boolean r = cityService.update(city, id);
//        
//        if (r) {
//            return Response.ok().status(Response.Status.NO_CONTENT).build();
//        } else {
//            return Response.notModified().build();
//        }           
//    }
//
//    @Path("/{id}")
//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response deleteCity(@PathParam("id") Long id) {
//
//        boolean r = cityService.delete(id);
//
//        if (r) {
//            return Response.ok().status(Response.Status.NO_CONTENT).build();
//        } else {
//            return Response.notModified().build();
//        }
//    }
}
