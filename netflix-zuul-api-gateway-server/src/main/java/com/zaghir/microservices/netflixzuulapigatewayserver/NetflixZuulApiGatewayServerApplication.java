package com.zaghir.microservices.netflixzuulapigatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;


/**on a besoin de configurer deux chose , 
 activer le proxy de Zuul    Zuul c'est l'implementation de spring cloud pour l'api gateways
 et declarer ce service chez eureka 
 
 on utilise l api gateways pour centraliser les fonctions ou les taches coummunes comme  : 
  - Authentification authaurization and security
  - rate limits 
  - fault Tolerances 
  - service Aggregation
  
  dans ce projet on va creer 
 */ 
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class NetflixZuulApiGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixZuulApiGatewayServerApplication.class, args);
	}
	
	/**
	 * creation d'un bean de type spring cloud Sleuth (notre detective) 
	 * qui va ajouter un identifiant unique pour chaque request qui passe pas l'api gateway Zuul
	 * ca permet d'identifier rapidement l'origine de probléme en car de probleme ou d'erreurs sur une request
	 * ca permet aussi de centraliser les traces pour mieux debugé
	 * log des traces request 
	 * port =8000 -->  2018-04-21 12:38:07.363  INFO [currency-exchange-service,5f485df37e39f591,5f485df37e39f591,true] 54688 --- [nio-8000-exec-3] c.z.m.c.CurrencyExchangeController       : ExchangeValue [id=10001, from=USD, to=INR, conversionMultiple=65.00, port=8000]
     * port =8765 --> 2018-04-21 12:40:54.538  INFO [netflix-zuul-api-gateway-server,37f7e7836dbc44fe,37f7e7836dbc44fe,true] 55152 --- [nio-8765-exec-6] c.z.m.n.ZuulLoggingFilter                : request --> org.springframework.cloud.netflix.zuul.filters.pre.Servlet30RequestWrapper@35bf84c8  request uri --> /currency-conversion-service/currency-converter-feign/from/USD/to/INR/quantity/100 
     * port =8765 --> 2018-04-21 12:40:54.552  INFO [netflix-zuul-api-gateway-server,37f7e7836dbc44fe,5f2c75e9351d0b98,true] 55152 --- [nio-8765-exec-2] c.z.m.n.ZuulLoggingFilter                : request --> org.springframework.cloud.netflix.zuul.filters.pre.Servlet30RequestWrapper@69933e75  request uri --> /currency-exchange-service/currency-exchange/from/USD/to/INR
     * sur chaque requette Sleuth ajoute une clé  5f2c75e9351d0b98 , 37f7e7836dbc44fe  pour identifier l'origine de la requette 
     * pour debuger il faut charcher dans tous les log des micro services avec les clés generer par Sleuth ce qui est un dificile [les logs sont distribués sur plusieurs endroits] 
     * c'est pour car on centralise les traces dans un seul endroit par Zipking  
	 */
	@Bean
	public Sampler defaultSampler(){
		return Sampler.ALWAYS_SAMPLE;
	}
}
