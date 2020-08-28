package edu.cornell.mannlib.vitro.webapp.cdi;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@ManagedBean
public class SystemOutService implements ExampleService {

  @PostConstruct
  public void initialize() {
    System.out.println("Initializing");
  }

  @PreDestroy
  public void cleanup() {
    System.out.println("Cleaning");
  }

  @Override
  public String test(String message) {
    System.out.println(message);
    return SystemOutService.class.getSimpleName();
  }

}
