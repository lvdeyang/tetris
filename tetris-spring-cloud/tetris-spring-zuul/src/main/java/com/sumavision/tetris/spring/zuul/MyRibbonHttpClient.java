package com.sumavision.tetris.spring.zuul;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang.BooleanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.feign.ribbon.FeignRetryPolicy;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.apache.RetryableRibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpResponse;
import org.springframework.cloud.netflix.ribbon.support.RetryableStatusCodeException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;

public class MyRibbonHttpClient extends RetryableRibbonLoadBalancingHttpClient {

	private LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory =
			new LoadBalancedRetryPolicyFactory.NeverRetryFactory();
	
	public MyRibbonHttpClient(IClientConfig config, ServerIntrospector serverIntrospector,
			LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory) {
		super(config, serverIntrospector, loadBalancedRetryPolicyFactory);
		this.loadBalancedRetryPolicyFactory = loadBalancedRetryPolicyFactory;
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public RibbonApacheHttpResponse execute(final RibbonApacheHttpRequest request, final IClientConfig configOverride) throws Exception {
		final RequestConfig.Builder builder = RequestConfig.custom();
		IClientConfig config = configOverride != null ? configOverride : this.config;
		builder.setConnectTimeout(config.get(
				CommonClientConfigKey.ConnectTimeout, this.connectTimeout));
		builder.setSocketTimeout(config.get(
				CommonClientConfigKey.ReadTimeout, this.readTimeout));
		builder.setRedirectsEnabled(config.get(
				CommonClientConfigKey.FollowRedirects, this.followRedirects));

		final RequestConfig requestConfig = builder.build();
		final LoadBalancedRetryPolicy retryPolicy = loadBalancedRetryPolicyFactory.create(this.getClientName(), this);
		RetryCallback retryCallback = new RetryCallback() {
			@Override
			public RibbonApacheHttpResponse doWithRetry(RetryContext context) throws Exception {
				//on retries the policy will choose the server and set it in the context
				//extract the server and update the request being made
				RibbonApacheHttpRequest newRequest = request;
				if(context instanceof LoadBalancedRetryContext) {
					ServiceInstance service = ((LoadBalancedRetryContext)context).getServiceInstance();
					if(service != null) {
						//Reconstruct the request URI using the host and port set in the retry context
						newRequest = newRequest.withNewUri(new URI(service.getUri().getScheme(),
								newRequest.getURI().getUserInfo(), service.getHost(), service.getPort(),
								newRequest.getURI().getPath(), newRequest.getURI().getQuery(),
								newRequest.getURI().getFragment()));
					}
				}
				if (isSecure(configOverride)) {
					final URI secureUri = UriComponentsBuilder.fromUri(newRequest.getUri())
							.scheme("https").build().toUri();
					newRequest = newRequest.withNewUri(secureUri);
				}
				HttpUriRequest httpUriRequest = newRequest.toRequest(requestConfig);
				final HttpResponse httpResponse = MyRibbonHttpClient.this.delegate.execute(httpUriRequest);
				if(retryPolicy.retryableStatusCode(httpResponse.getStatusLine().getStatusCode())) {
					throw new RetryableStatusCodeException(MyRibbonHttpClient.this.clientName,
							httpResponse.getStatusLine().getStatusCode());
				}
				return new RibbonApacheHttpResponse(httpResponse, httpUriRequest.getURI());
			}
		};
		return this.executeWithRetry(request, retryPolicy, retryCallback);
	}
	
	private RibbonApacheHttpResponse executeWithRetry(RibbonApacheHttpRequest request, LoadBalancedRetryPolicy retryPolicy, RetryCallback<RibbonApacheHttpResponse, IOException> callback) throws Exception {
		RetryTemplate retryTemplate = new RetryTemplate();
		boolean retryable = request.getContext() == null ? true :
				BooleanUtils.toBooleanDefaultIfNull(request.getContext().getRetryable(), true);
		retryTemplate.setRetryPolicy(retryPolicy == null || !retryable ? new NeverRetryPolicy()
				: new FeignRetryPolicy(request, retryPolicy, this, this.getClientName()));
		retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());
		return retryTemplate.execute(callback);
	}

}
