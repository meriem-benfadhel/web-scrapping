package com.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.business.entity.Business;
import com.business.service.BusinessService;

@Component
public class BusinessRoute extends RouteBuilder{
	 
	@Override
	public void configure() throws Exception {

				        restConfiguration()
				        .enableCORS(true)
		                .corsAllowCredentials(true) // <-- Important
		                .corsHeaderProperty("Access-Control-Allow-Origin","*")
		                .corsHeaderProperty("Access-Control-Allow-Headers","Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization")
                
		                .contextPath(env.getProperty("camel.component.servlet.mapping.contextPath", "/rest/*"))
		                .apiContextPath("/api-doc")
		                .apiProperty("api.title", "Spring Boot Camel  Rest API.")
		                .apiProperty("api.version", "1.0")
		                .apiProperty("cors", "true")
		                .apiContextRouteId("doc-api")
		                .bindingMode(RestBindingMode.json)
		                .host("0.0.0.0")
		                .port(8080);
		               
		                
		                
		               

		        rest("/business")
		                .consumes(MediaType.APPLICATION_JSON_VALUE)
		                .produces(MediaType.APPLICATION_JSON_VALUE)
		                .get("/{name}").route()
		                .to("{{route.findBusinessByName}}")
		                
		                .endRest()
		                .get("/").route()
		                .to("{{route.findAllBusinesses}}")
		                
		                .endRest()
		                .post("/").route()
		                .marshal().json()
		                .unmarshal(getJacksonDataFormat(Business.class))
		                .to("{{route.saveBusiness}}")
		                .endRest()
		                
		                .delete("/{businessId}").route()
		                .to("{{route.removeBusiness}}")
		                .end();
		                //.setHeader("Origin",constant("http://localhost:8080"));

		        from("{{route.findBusinessByName}}")
		                .log("Received header : ${header.name}")
		                .bean(BusinessService.class, "findBusinessByName(${header.name})");

		        from("{{route.findAllBusinesses}}")
		                .bean(BusinessService.class, "findAllBusinesses");


		        from("{{route.saveBusiness}}")
		                .log("Received Body ${body}")
		                .bean(BusinessService.class, "addBusiness(${body})");


		        from("{{route.removeBusiness}}")
		                .log("Received header : ${header.businessId}")
		                .bean(BusinessService.class, "removeBusiness(${header.businessId})");
		       
		      
		        
		    }

		    private JacksonDataFormat getJacksonDataFormat(Class<?> unmarshalType) {
		        JacksonDataFormat format = new JacksonDataFormat();
		        format.setUnmarshalType(unmarshalType);
		        return format;
		    }
		    private final Environment env;
			  public BusinessRoute(Environment env) {
			        this.env = env;
			    }

		
	
	}

