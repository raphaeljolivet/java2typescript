package com.example;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.example.config.AppConfig;

public class Starter {
  public static void main(final String[] args) throws Exception {

    Server server = new Server(8080);
    HandlerCollection handlerCollection = new HandlerCollection();
    server.setHandler(handlerCollection);

    addSpringContext(handlerCollection);

    // Default one, resourceHandler takes everything
    addStaticContext(handlerCollection);

    server.start();
    server.join();
  }

  static private void addSpringContext(HandlerCollection collection) {
    final ServletHolder servletHolder = new ServletHolder(new CXFServlet());
    final ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");

    context.addServlet(servletHolder, "/rest/*");
    context.addEventListener(new ContextLoaderListener());

    context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
    context.setInitParameter("contextConfigLocation", AppConfig.class.getName());

    collection.addHandler(context);
  }

  static private void addStaticContext(HandlerCollection collection) {
    ResourceHandler resHandler = new ResourceHandler();
    resHandler.setWelcomeFiles(new String[] { "index.html" });
    try {
      resHandler.setBaseResource(new org.eclipse.jetty.util.resource.FileResource(Starter.class
          .getResource("/WEB-INF/")));
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    collection.addHandler(resHandler);
  }
}
