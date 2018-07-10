package com.kooppi.nttca.wallet.config.rest;

import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

import com.kooppi.nttca.ce.payment.resources.PaymentResources;
import com.kooppi.nttca.portal.common.exception.handler.ExceptionMappingFilter;
import com.kooppi.nttca.portal.common.filter.response.AuditLogResponseFilter;
import com.kooppi.nttca.portal.common.filter.response.PortalResponseMessageFilter;
import com.kooppi.nttca.wallet.rest.CurrencyRate.resources.CurrencyRateResources;
import com.kooppi.nttca.wallet.rest.UsageStatistics.resources.UsageStatisticsResources;
import com.kooppi.nttca.wallet.rest.adjustment.resources.AdjustmentResources;
import com.kooppi.nttca.wallet.rest.adjustment.resources.CommonAdjustmentResources;
import com.kooppi.nttca.wallet.rest.organization.resources.OrganizationResources;
import com.kooppi.nttca.wallet.rest.priceBook.resources.PriceBookResources;
import com.kooppi.nttca.wallet.rest.report.resources.ReportResources;
import com.kooppi.nttca.wallet.rest.reservation.resources.TransactionReservationResources;
import com.kooppi.nttca.wallet.rest.tnc.resources.TnCResources;
import com.kooppi.nttca.wallet.rest.transaction.resources.TransactionResources;
import com.kooppi.nttca.wallet.rest.wallet.resources.WalletResources;

import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("1.0")
public class RestConfig extends ResourceConfig {

	public RestConfig(){
		register(AdjustmentResources.class);
		register(CommonAdjustmentResources.class);
		register(TransactionReservationResources.class);
		register(TransactionResources.class);
		register(UsageStatisticsResources.class);
//		register(UserWalletResources.class);
		register(WalletResources.class);
		register(CurrencyRateResources.class);
		register(PriceBookResources.class);
		register(PaymentResources.class);
		register(TnCResources.class);
		register(ReportResources.class);
		register(OrganizationResources.class);
		register(ExceptionMappingFilter.class);
		LoggingFilter loggerFilter = new LoggingFilter(Logger.getLogger(LoggingFilter.class.getName()), true);
		register(loggerFilter);
		register(PortalResponseMessageFilter.class);
//		register(PortalRequestHeaderFilter.class);
		register(AuthenticationFilter.class);
		//audit log filter
		register(AuditLogResponseFilter.class);
		setupMOXyJsonBinding();
		
		register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		
		BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setBasePath("credit-module-wallet-ws/1.0");
        beanConfig.setResourcePackage("com.kooppi.nttca");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
	}
	
	 private void setupMOXyJsonBinding() {
        final MoxyJsonConfig moxyConfig = new MoxyJsonConfig();
        moxyConfig.setFormattedOutput(true);
        moxyConfig.setMarshalEmptyCollections(true);
        register(moxyConfig.resolver());
	 }
}
