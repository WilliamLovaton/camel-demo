package com.compartamos.controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ApiController {

    @Inject
    ProducerTemplate producerTemplate;

    @GET
    @Path("/")
    public Response get() {
        Exchange exchange = producerTemplate.send("direct:get", e -> {
            e.getMessage().setHeader("operationName", "get");
        });

        return response(exchange);
    }
    
    @GET
    @Path("/proxy1")
    public Response proxy1() {
        Exchange exchange = producerTemplate.send("direct:proxy1", e -> {
            e.getMessage().setHeader("operationName", "proxy1");
        });

        return response(exchange);
    }

    @GET
    @Path("/proxy2")
    public Response proxy2() {
        Exchange exchange = producerTemplate.send("direct:proxy2", e -> {
            e.getMessage().setHeader("operationName", "proxy2");
        });

        return response(exchange);
    }

    @GET
    @Path("/proxy3")
    public Response proxy3() {
        Exchange exchange = producerTemplate.send("direct:proxy3", e -> {
            e.getMessage().setHeader("operationName", "proxy3");
        });

        return response(exchange);
    }

    @GET
    @Path("/proxy4")
    public Response proxy4() {
        Exchange exchange = producerTemplate.send("direct:proxy4", e -> {
            e.getMessage().setHeader("operationName", "proxy4");
        });

        return response(exchange);
    }

    private Response response(Exchange exchange) {
        if (exchange.getException() != null) {
            return Response.serverError().entity(exchange.getException().getMessage()).build();
        }
        return Response.ok(exchange.getMessage().getBody()).build();
    }
}
