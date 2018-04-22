package com.zaghir.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zaghir.microservices.currencyconversionservice.bean.CurrencyConversionBean;

@RestController
public class CurrencyConversionController {
	
	private Logger log  = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CurrencyExchangeServiceProxy proxy ;
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(
			@PathVariable String from ,
			@PathVariable String to ,
			@PathVariable BigDecimal quantity){
		Map<String , String> uriVariables = new HashMap();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> reponseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}",				 
				CurrencyConversionBean.class ,
				uriVariables);
		CurrencyConversionBean response = reponseEntity.getBody();
		
		return new CurrencyConversionBean(
				response.getId(),
				from,
				to,
				response.getConversionMultiple(),
				quantity ,
				quantity.multiply(response.getConversionMultiple()),
				response.getPort());
	}
	
	/**
	 * 
	 * ulilisation de feign comme proxy pour centraliser les appelles des api rest 
	 * feign est utilisé à la place se restTemplate qui a besoin d'etre parametré avec url le mapper et les param de la requet 
	 */
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(
			@PathVariable String from ,
			@PathVariable String to ,
			@PathVariable BigDecimal quantity){
		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);
		log.info("{}",response);
		return new CurrencyConversionBean(
				response.getId(),
				from,
				to,
				response.getConversionMultiple(),
				quantity ,
				quantity.multiply(response.getConversionMultiple()),
				response.getPort());
	}

}
