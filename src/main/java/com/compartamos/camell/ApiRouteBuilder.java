package com.compartamos.camell;

import java.text.SimpleDateFormat;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiRouteBuilder extends RouteBuilder {
	
    private String restEndpoint;
    private Integer timeoutDefault = 30000;

    @Override
    public void configure() {
    	
    	configureTimeout();
        
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
    
    public void configureTimeout() {

		HttpComponent httpsComponent = getContext().getComponent("https4", HttpComponent.class);
		HttpComponent httpComponent = getContext().getComponent("http4", HttpComponent.class);
		httpsComponent.setConnectionTimeToLive(timeoutDefault);
		httpsComponent.setSocketTimeout(timeoutDefault);
		httpsComponent.setConnectTimeout(timeoutDefault);
		httpComponent.setConnectionTimeToLive(timeoutDefault);
		httpComponent.setSocketTimeout(timeoutDefault);
		httpComponent.setConnectTimeout(timeoutDefault);
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
