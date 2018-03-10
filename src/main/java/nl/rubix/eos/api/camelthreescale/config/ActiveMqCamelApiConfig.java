package nl.rubix.eos.api.camelthreescale.config;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

public class ActiveMqCamelApiConfig implements PropertyFileConfig {
  @Override public String getPropertyFileName() {
    return "activemq-camel-api.properties";
  }

  @Override public boolean isOptional() {
    return true;
  }
}

