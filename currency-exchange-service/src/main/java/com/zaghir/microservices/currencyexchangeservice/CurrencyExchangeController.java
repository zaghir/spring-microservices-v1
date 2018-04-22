package com.zaghir.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.zaghir.microservices.currencyexchangeservice.bean.ExchangeValue;
import com.zaghir.microservices.currencyexchangeservice.dao.ExchangeValueRepository;

@RestController
public class CurrencyExchangeController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Environment environment ;
	
	@Autowired
	ExchangeValueRepository repository ;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public ExchangeValue retrieveExchangeValue(@PathVariable String from , @PathVariable String to){
		
		/* cette valeur est on dur*/
		//ExchangeValue exchangeValue = new ExchangeValue(1000L, from ,to , BigDecimal.valueOf(65));
		
		/*cette valeur est recuperé par une dao*/
		ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
		
		/* le port serveur est recuperé depuis la configuration de l'application */
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		log.info("{}" ,exchangeValue) ;
		return exchangeValue ;
	}
}
