package se.jensen.johanna.fakestorecartservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

  @Value("${product-service-url}")
  private String productServiceBaseUrl;

  @Bean
  ProductClient productClient(RestClient.Builder builder) {
    RestClient restClient = builder.baseUrl(productServiceBaseUrl).build();
    HttpServiceProxyFactory factory =
        HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
            .build();

    return factory.createClient(ProductClient.class);
  }


}
