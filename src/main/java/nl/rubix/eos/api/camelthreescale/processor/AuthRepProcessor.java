package nl.rubix.eos.api.camelthreescale.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import threescale.v3.api.AuthorizeResponse;
import threescale.v3.api.ParameterMap;
import threescale.v3.api.ServerError;
import threescale.v3.api.ServiceApi;
import threescale.v3.api.impl.ServiceApiDriver;

import javax.inject.Inject;
import javax.inject.Named;

@Named("authRepProcessor")
public class AuthRepProcessor implements Processor {

  @Inject
  @ConfigProperty(name = "SERVICE_TOKEN")
  private String serviceToken;

  @Inject
  @ConfigProperty(name = "SERVICE_ID")
  private String serviceId;

  @Override
  public void process(Exchange exchange) throws Exception {
    String appId = exchange.getIn().getHeader("appId", String.class);
    String appKey = exchange.getIn().getHeader("appKey", String.class);

    AuthorizeResponse authzResponse = authrep(createParametersMap(appId, appKey));

    if(authzResponse.success() == false) {
      exchange.setProperty(Exchange.ROUTE_STOP, true);
      exchange.getIn().setHeader("authz:errorCode", authzResponse.getErrorCode());
      exchange.getIn().setHeader("authz:reason", authzResponse.getReason());
    }

  }

  private ParameterMap createParametersMap(String appId, String appKey) {
    ParameterMap params = new ParameterMap();
    params.add("app_id", appId);
    params.add("app_key", appKey);

    ParameterMap usage = new ParameterMap();
    usage.add("hits", "1");
    params.add("usage", usage);

    return params;
  }

  private AuthorizeResponse authrep(ParameterMap params) {

    ServiceApi serviceApi = ServiceApiDriver.createApi();

    AuthorizeResponse response = null;

    try {
      response = serviceApi.authrep(serviceToken, serviceId, params);
    } catch (ServerError serverError) {
      serverError.printStackTrace();
      throw new RuntimeCamelException(serverError.getMessage(), serverError.getCause());
    }
    return response;
  }
}