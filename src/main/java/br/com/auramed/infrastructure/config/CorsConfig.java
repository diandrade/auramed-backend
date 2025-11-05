package br.com.auramed.infrastructure.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.List;

@Provider
public class CorsConfig implements ContainerResponseFilter {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:5173",
            "http://localhost:5174",
            "https://auramed-sem2-front.vercel.app",
            "https://auramed-backend-6yw9.onrender.com"
    );

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString("Origin");

        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
        } else {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
        }

        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add(
                "Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, x-requested-with, cache-control, access-control-allow-origin, access-control-allow-credentials"
        );
        responseContext.getHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH"
        );
        responseContext.getHeaders().add("Access-Control-Max-Age", "86400"); // 24 horas

        responseContext.getHeaders().add("Access-Control-Expose-Headers",
                "authorization, content-type, location");

        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(200);
        }
    }
}