package nl.rubix.eos.api.camelthreescale;

import io.fabric8.annotations.Alias;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

import javax.inject.Inject;

@ContextName("activemq-camel-api")
public class ActiveMqCamelApi extends RouteBuilder{

  @Inject
  @Alias("jms")
  private ActiveMQComponent activeMQComponent;

  @Override
  public void configure() throws Exception {
      from("jms:queue:test")
        .log("received message")
        .process("authRepProcessor")
        .log("request authenticated and authorized");
  }
}
