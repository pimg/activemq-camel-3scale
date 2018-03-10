package nl.rubix.eos.api.camelthreescale.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

public class ActiveMQComponentConnectionFactory {

  @Inject
  @ConfigProperty(name = "AMQ_USER")
  private String amqUser;

  @Inject
  @ConfigProperty(name = "AMQ_PASSWORD")
  private String amqPassword;

  @Inject
  @ConfigProperty(name = "AMQ_SERVICE")
  private String amqService;

  @Produces
  @Named("connectionFactory")
  @ApplicationScoped
  private ActiveMQConnectionFactory configureActiveMQConnectionFactory() {
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(amqService);
    factory.setUserName(amqUser);
    factory.setPassword(amqPassword);
    factory.setRedeliveryPolicy(setRedeliveryPolicy());
    return factory;
  }

  private RedeliveryPolicy setRedeliveryPolicy() {
    RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();

    redeliveryPolicy.setInitialRedeliveryDelay(1000);
    redeliveryPolicy.setRedeliveryDelay(2000);
    redeliveryPolicy.setUseExponentialBackOff(true);
    redeliveryPolicy.setBackOffMultiplier(2);
    redeliveryPolicy.setMaximumRedeliveryDelay(300000);
    redeliveryPolicy.setMaximumRedeliveries(-1);

    return redeliveryPolicy;
  }

}
