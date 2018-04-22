package com.zaghir.microservices.currencyconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zaghir.microservices.currencyconversionservice.bean.CurrencyConversionBean;

/**on va utiliser ribbon qui va gerer sur quel server on va utiliser pour faire l'appel à l'api rest
 * le proxy feign ne gere plus ca*/
//@FeignClient(name="currency-exchange-service", url="localhost:8000")

/**
 * au début le proxy Feing utilise directement une url pour utiliser le micro service dans l'application currency-exchange-service   
 */
//@FeignClient(name="currency-exchange-service")
/**
 * on a configurer L'api gateway on veut que proxy passe par le proxy de l'api gateway Zuul pour utiliser le mirco service
 * grace au nom de l'application est eureka server on peut faire ce changement 
 */
@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

	/**
	 * il faut aussi changer le chemin url de micro service on ajout le nom de l'application
	 * pour tester 
	 * http://localhost:8100/currency-converter-feign/from/USD/to/INR/quantity/100
	 *    avec l'api gateway zuul
	 * http://localhost:8765/currency-conversion-service/currency-converter-feign/from/USD/to/INR/quantity/100
	 * on recupere les traces resquet dans le filterLogging de zuul
	 */
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
	@GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from , @PathVariable("to") String to);
}
