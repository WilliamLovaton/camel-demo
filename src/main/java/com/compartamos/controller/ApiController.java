package com.compartamos.controller;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface ApiController {
   
    
    @GET
    @Path("/")
    public Response get();
    
    @GET
    @Path("/proxy1")
    public Response proxy1();

    @GET
    @Path("/proxy2")
    public Response proxy2();

    @GET
    @Path("/proxy3")
    public Response proxy3();

    @GET
    @Path("/proxy4")
    public Response proxy4();

    
}
