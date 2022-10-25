package fr.projetPersonnel;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.projetPersonnel.entities.Patient;
import fr.projetPersonnel.repositories.PatientRepository;

@SpringBootApplication
public class PatientSpringBootMvcSecThymeleaf1Application {

	public static void main(String[] args) {
		SpringApplication.run(PatientSpringBootMvcSecThymeleaf1Application.class, args);
	}
	
	//@Bean
	CommandLineRunner commandLineRunner(PatientRepository patientRepository) {
		return arg ->{
			patientRepository.save(
					new Patient(null,"pascal",new Date(),false,12));
			
			patientRepository.save(
					new Patient(null,"joseph",new Date(),true,1));
			patientRepository.save(
					new Patient(null,"thomas",new Date(),false,2));
			patientRepository.save(
					new Patient(null,"paul",new Date(),true,62));
			patientRepository.save(
					new Patient(null,"fidele",new Date(),true,62));
			patientRepository.save(
					new Patient(null,"david",new Date(),true,62));
			patientRepository.save(
					new Patient(null,"pasto",new Date(),true,62));
			
			//liste les patients
			patientRepository.findAll().
			forEach(
					p->
					{
						System.out.println("nom du patient: "+p.getNom());
					}
					);
		};
		
	}

}
