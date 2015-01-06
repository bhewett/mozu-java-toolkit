package com.mozu.base.security;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mozu.api.MozuConfig;
import com.mozu.api.contracts.appdev.AppAuthInfo;
import com.mozu.api.security.AppAuthenticator;
import com.mozu.api.utils.MozuHttpClientPool;
import com.mozu.logger.MozuAppLogger;

@Component
public class MozuAppAuthenticator {
	private static final MozuAppLogger logger = MozuAppLogger.getLogger(MozuAppAuthenticator.class);
	
	@Value("${ApplicationId}")
	String applicationId;
    @Value("${SharedSecret}")
    String sharerdSecret;
    @Value("${BaseAuthAppUrl}")
    String baseAppAuthUrl;
	
	@PostConstruct
	public void appAuthentication() {
		
		logger.info("Authenticating Application in Mozu...");
		try {
			
            AppAuthInfo appAuthInfo = new AppAuthInfo();
            appAuthInfo.setApplicationId(applicationId);
            appAuthInfo.setSharedSecret(sharerdSecret);
            if (!StringUtils.isEmpty(baseAppAuthUrl))
            	MozuConfig.setBaseUrl(baseAppAuthUrl);
            AppAuthenticator.initialize(appAuthInfo);
            logger.info("Auth ticket : "+AppAuthenticator.getInstance().getAppAuthTicket().getAccessToken());
            logger.info("Application authenticated");
		} catch(Exception exc) {
			logger.error(exc.getMessage(), exc);
		}
		
	}

    @PreDestroy
    public void cleanup () {
        logger.debug("Shutdown HttpClient connection manager.");
        MozuHttpClientPool.getInstance().shutdown();
    }
}
