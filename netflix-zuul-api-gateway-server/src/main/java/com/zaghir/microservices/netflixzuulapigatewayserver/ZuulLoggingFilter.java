package com.zaghir.microservices.netflixzuulapigatewayserver;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/***
 * 
 * Creation de service de log implementé dans l'api gateway  Zuul
 * 
 * pour tester le filter
 * il faut lancer 
 * 1 - le serveur Eureka pour le namingServer  qui recuper tous les services activés et fournir la liste des micro Service
 * 2 - lancer les micro service 
 * 3 - lancer le service de l'api gateway Zuul qui execute le filter de logging Request
 * 4 - adresse de test de microservice locahost:8000/currency-exchange/from/EUR/to/INR
 * 5 - adresse de serveur eureka localhost:8761 
 * exp : 1- NetflixEurekaNamingServerApplication
 *       2- CurrencyExchangeServiceApplication8000
 *       3- CurrencyExchangeServiceApplication8100
 *       4- NetflixZuulApiGatewayServerApplication
 *       
 * maintenant on va passer par l'api Gateway Zuul pour utilier les micro services developper {currency-exchange ....}
 * 
 * zuul url          == http://localhost:8765/{application-name}/{uri}
 * url mirocservice  == http://localhost:8000/currency-exchange/from/EUR/to/INR
 * 
 * zuul est cablé eureka et a partir de eureka server recupe les nom des applications ==> {application-name}
 *      et recuper l {uri} a partir de la quest 
 * zuul url devient   == http://localhost:8765/currency-exchange-service/currency-exchange/from/EUR/to/INR                
 * c'est l'url de microservice a partir de l'api gateway
 * dans le filer loggingFilter on peut voir les traces de request 
 * zuul api gateway proxy
 */

@Component
public class ZuulLoggingFilter extends ZuulFilter {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public Object run() throws ZuulException {
		/**
		 * 4- eme  etape de configuration de filter
		 * ici en defini notre logique de notre filter
		 * exp :on veut logger les details des requettes 
		 */
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		log.info("request --> {}  request uri --> {} " , request ,request.getRequestURI());
		return null;
	}

	@Override
	public boolean shouldFilter() {
		/**
		 * 2- eme  etape de configuration de filter
		 * ici on specifie s'il faut executer le filter ou pas
		 * on peut metre des tests avant d'activer ou pas le filter
		 * ici on execute le filter dans tous les cas
		 */
		return true;
	}

	@Override
	public int filterOrder() {
		/**
		 * 1- ere etape de configuration de filter
		 * si on a plusieurs  filer , { securityFilter , loggingFilter ....}
		 * on peut specifier l'ordre de priorité ou d'execution des tous ces filtres 
		 * dans la valeur de retour la methode filterOrder()
		 * ici c'est le premier filter qui va etre executer par la gateway Zuul
		 * */
		return 1;
	}

	@Override
	public String filterType() {
		/**
		 * 3- eme etape de configuration de filter
		 * ici on specifie si le filter doit etre execute 
		 * avant l'execution de la request ==> return "pre"
		 * apres l'execution de la request ==> return "post"
		 * ou seulement s'il y a une error request , la requette provoque une erreur ==> return "error"
		 */
		return "pre";
	}

}
