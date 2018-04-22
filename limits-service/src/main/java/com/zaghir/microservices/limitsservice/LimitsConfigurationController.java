package com.zaghir.microservices.limitsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zaghir.microservices.limitsservice.bean.LimitConfiguration;

@RestController
public class LimitsConfigurationController {
	
	@Autowired
	Configuration configuration;
	
	@GetMapping("/limits")
	public LimitConfiguration retrieveLimitsFromConfigurations(){
		/** les valeurs de configuration en dure */ 
		//return new LimitConfiguration(1, 1000);
		/** les valeurs de configuration recuperé depuis une classe de configuration 
		 * et les valeurs sont enregistrées dans le fichier application.properties sufixé par limits-service */
		return new LimitConfiguration(configuration.getMinimum(), configuration.getMaximum());
	}
	
	/**
	 * creation de service avec une tolerance au panne gerer par Hystrix
	 * ici le traitement est simple ,le service renvoie une exception quand il est appelé 
	 * Hystrix detecte la panne sur le service et fait apple à une methode pour fournir 
	 * une reponse par defaut configuer au cas ou le service et hors service ou bloqué
	 * ici Hystrix fait appel à la methode fallbackRetrieveConfiguration() quand il y a un probleme
	 * c'est le comportement de gestion de pannes on ne veut envoyer une exception mais une reponse minimal
	 * que les autres micro services utilisent et ne pas etres bloqué
	 * ceci est parametre grace à la propriété fallbackMethod de Hystrix 
	 * pour faire le test :
	 * 1- demarrer le config-server
	 * 2- demarrer le service limits-service
	 * 3- on test le service  fault-tolerance-example qui renvoye dans tous les cas une exception 
	 *       url = http://localhost:8080/fault-tolerance-example
	 *       on recupere un objet Json avec les valeurs configurer dans fallbackRetrieveConfiguration 
	 *            ==>  { "minimum":999  , "maximum":9}
	 */
	@GetMapping("/fault-tolerance-example")
	@HystrixCommand(fallbackMethod="fallbackRetrieveConfiguration")
	public LimitConfiguration retrieveConfiguration(){
		throw new RuntimeException("Not availible ");
	}
	
	public LimitConfiguration fallbackRetrieveConfiguration(){
		return new LimitConfiguration(999, 9) ;
	}

}
