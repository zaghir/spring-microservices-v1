package com.zaghir.microservices.currencyconversionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableFeignClients("com.zaghir.microservices.currencyconversionservice")
@EnableDiscoveryClient
public class CurrencyConversionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionServiceApplication.class, args);
	}
	
	/**
	 * creation d'un bean de type spring cloud Sleuth (notre detective) 
	 * qui va ajouter un identifiant unique pour chaque request qui utilise ce micro service
	 * ca permet d'identifier rapidement l'origine de probléme en car de probleme ou d'erreurs sur une request
	 * ca permet aussi de centraliser les traces pour mieux debugé 
	 */
	@Bean
	public Sampler defaultSampler(){
		return Sampler.ALWAYS_SAMPLE;
	}
}
