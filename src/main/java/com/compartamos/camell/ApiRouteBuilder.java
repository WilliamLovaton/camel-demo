package com.compartamos.camell;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.text.SimpleDateFormat;

public class ApiRouteBuilder extends RouteBuilder {

    private String restEndpoint;

    @Override
    public void configure() {
    	
        from(restEndpoint).routeId("routeMain")
           .toD("direct:${header.operationName}")
	    .end();
        
        from("direct:get").routeId("routeGet")
            .process(exchange -> {
                Language result = new Language();
                result.setId(1);
                result.setName("Love");
                result.setDescription("Programming");
                exchange.getOut().setBody(entityToJson(result));	
            })
        .end();   

        from("direct:proxy1").routeId("routeproxy1")     
            .process(exchange -> {
                exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                exchange.getOut().setHeader(Exchange.HTTP_METHOD, "GET");                     
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);                
            })
            .removeHeader(Exchange.HTTP_PATH)  
            .to("{{webproxy2}}" + "?bridgeEndpoint=true&throwExceptionOnFailure=false")   
            .process(exchange -> {
                exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                exchange.getOut().setHeader(Exchange.HTTP_METHOD, "GET");                     
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);                
            })
            .removeHeader(Exchange.HTTP_PATH)  
            .recipientList(constant("{{webproxy3}}" + "?bridgeEndpoint=true&throwExceptionOnFailure=false"))
        .end();        
        
        from("direct:proxy2").routeId("routeproxy2")
            .process(exchange -> {
                exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                exchange.getOut().setHeader(Exchange.HTTP_METHOD, "GET");                     
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);                
            })
            .removeHeader(Exchange.HTTP_PATH)
	        .recipientList(constant("{{webproxy4}}" + "?bridgeEndpoint=true&throwExceptionOnFailure=false"))
        .end(); 

        from("direct:proxy3").routeId("routeproxy3")
            .process(exchange -> {
                exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                exchange.getOut().setHeader(Exchange.HTTP_METHOD, "GET");                     
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);                
            })
            .removeHeader(Exchange.HTTP_PATH)
	        .recipientList(constant("{{webapi}}" + "?bridgeEndpoint=true&throwExceptionOnFailure=false"))       
        .end();         
        
        from("direct:proxy4").routeId("routeproxy4")
            .process(exchange -> {
                exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                exchange.getOut().setHeader(Exchange.HTTP_METHOD, "GET");                     
                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);                
            })
            .removeHeader(Exchange.HTTP_PATH)
	        .recipientList(constant("{{webapi}}" + "?bridgeEndpoint=true&throwExceptionOnFailure=false"))       
        .end(); 

    }

    public void setRestEndpoint(String restEndpoint) {
        this.restEndpoint = restEndpoint;
    }

    public String entityToJson(Object value) throws Exception {

        if (value == null) {
            return "";
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        return mapper.writeValueAsString(value);
    }
}
