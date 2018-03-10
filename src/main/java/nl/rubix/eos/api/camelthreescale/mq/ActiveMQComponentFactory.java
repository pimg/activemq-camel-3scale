package nl.rubix.eos.api.camelthreescale.mq;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.jms.connection.*;
import org.springframework.transaction.PlatformTransactionManager;

@ApplicationScoped
public class ActiveMQComponentFactory {

  @Produces
  @Named("jms")
  @ApplicationScoped
  public ActiveMQComponent create(@Named("jmsConfiguration") JmsConfiguration configuration) {
    ActiveMQComponent component = new ActiveMQComponent();
    component.setConfiguration(configuration);
    return component;
  }

  @Produces
  @Named("jmsConfiguration")
  @ApplicationScoped
  private JmsConfiguration createJMSConfiguration(@Named("cachingConnectionFactory") CachingConnectionFactory factory, @Named("jmsTxManager") JmsTransactionManager txManager) {
    JmsConfiguration jmsConfiguration = new JmsConfiguration();
    jmsConfiguration.setConnectionFactory(factory);
    jmsConfiguration.setTransacted(true);
    jmsConfiguration.setTransactionManager(txManager);
    jmsConfiguration.setCacheLevelName("CACHE_CONSUMER");

    return jmsConfiguration;
  }

  @Produces
  @Named("cachingConnectionFactory")
  @ApplicationScoped
  private CachingConnectionFactory createCachingConnectionFactory(@Named("connectionFactory") ActiveMQConnectionFactory factory) {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(factory);
    return cachingConnectionFactory;
  }

  @Produces
  @Named("jmsTxManager")
  @Singleton
  private JmsTransactionManager createTransactionManager(@Named("cachingConnectionFactory") CachingConnectionFactory factory) {
    JmsTransactionManager jmsTxManager = new JmsTransactionManager();
    jmsTxManager.setConnectionFactory(factory);
    return jmsTxManager;
  }

  @Produces
  @Named("PROPAGATION_REQUIRED")
  @ApplicationScoped
  SpringTransactionPolicy createSpringTxPolicy(@Named("jmsTxManager") PlatformTransactionManager txManager) {
    SpringTransactionPolicy txPolicy = new SpringTransactionPolicy(txManager);
    txPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
    return txPolicy;
  }

}