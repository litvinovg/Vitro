package edu.cornell.mannlib.vitro.webapp.controller.api;

import java.io.IOException;

import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.servlet.ServletException;

import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import edu.cornell.mannlib.vitro.webapp.cdi.ExampleService;
import edu.cornell.mannlib.vitro.webapp.cdi.SystemOutService;
import stubs.javax.servlet.http.HttpServletRequestStub;
import stubs.javax.servlet.http.HttpServletResponseStub;


@EnableWeld
public class ExampleControllerTest {

  @WeldSetup
  public WeldInitiator weld = WeldInitiator.from(ExampleControllerTest.class, ExampleController.class).addBeans(createBarBean()).build();

  static Bean<?> createBarBean() {
    return MockBean.builder()
        .types(ExampleService.class)
        .creating(new SystemOutService())
        .build();
  }

  @Inject
  private ExampleController exampleController;

  @Test
  public void testDoGet() throws ServletException, IOException {
    HttpServletRequestStub request = new HttpServletRequestStub();
    HttpServletResponseStub response = new HttpServletResponseStub();
    exampleController.doGet(request, response);
    Assert.assertEquals(response.getOutput(), SystemOutService.class.getSimpleName());
  }

}
