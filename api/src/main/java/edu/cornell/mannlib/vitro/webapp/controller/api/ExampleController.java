package edu.cornell.mannlib.vitro.webapp.controller.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cornell.mannlib.vitro.webapp.cdi.ExampleService;

@WebServlet(name = "Example", urlPatterns = { "/example" })
public class ExampleController extends HttpServlet {

  private static final long serialVersionUID = 394765293450347538L;

  @Inject
  private ExampleService exampleService;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.getWriter().write(exampleService.test(String.format("GET /example %s injected", exampleService.getClass().getSimpleName())));
  }
  
}
