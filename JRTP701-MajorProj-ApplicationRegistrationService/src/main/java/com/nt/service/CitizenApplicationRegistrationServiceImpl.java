package com.nt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.exceptions.InvalidSSNException;
import com.nt.repository.IApplicationRegistrationRepository;

import reactor.core.publisher.Mono;

@Service
public class CitizenApplicationRegistrationServiceImpl implements ICitizenApplicationRegistrationService {
    @Autowired
	private IApplicationRegistrationRepository citizenRepo;
	/*   @Autowired
	private RestTemplate template;*/
    @Autowired
    private WebClient client;
    @Value("${ar.ssa-web.url}")
    private String endpointUrl;
    @Value("${ar.state}")
    private String targetState;
   

	@Override
	public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs) throws InvalidSSNException {
		
		//perform web service call to check wheater ssn number is valid or not to get the state name(using RestTemplate)
		//ResponseEntity<String> response=template.exchange(endpointUrl,HttpMethod.GET, null,String.class,inputs.getSsn());
		
		//perform web service call to check wheater ssn number is valid or not to get the state name(using WebClient)
		//get state name
		/*String stateName=client.get().uri(endpointUrl,inputs.getSsn()).retrieve().bodyToMono(String.class).block();
		
		//register citizen if he belongs to california state
		if(stateName.equalsIgnoreCase(targetState)){
			//prepare tthe entity object
			CitizenAppRegistrationEntity entity=new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			//save the object
			int appId=citizenRepo.save(entity).getAppId();
			return appId;
		}
		throw new InvalidSSNException("invalid SSN");
		*/
		
		//perform web service call to check wheater ssn number is valid or not to get the state name(using WebClient)
				//get state name
		//code improvisation
		Mono<String> response=client.get().uri(endpointUrl,inputs.getSsn()).retrieve()
				                                 .onStatus(HttpStatus.BAD_REQUEST::equals,res->res.bodyToMono(String.class)
				                                		 .map(ex->new InvalidSSNException("invalid ssn"))).bodyToMono(String.class);
		
		String stateName=response.block();
		//register citizen if he belongs to california state(CA)
		if(stateName.equalsIgnoreCase(targetState)) {
			//prepare the entity object
			CitizenAppRegistrationEntity entity=new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			//save the object
			int appId=citizenRepo.save(entity).getAppId();
			return appId;
		}//if
		throw new InvalidSSNException("invalid ssn");
		
	}

}





