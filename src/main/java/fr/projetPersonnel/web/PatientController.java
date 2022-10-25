package fr.projetPersonnel.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.projetPersonnel.entities.Patient;
import fr.projetPersonnel.repositories.PatientRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PatientController {

	PatientRepository patientRepository;
	
	@GetMapping(path = "/index")
	public String patients(Model model,
			@RequestParam(name="page",defaultValue = "0") int page,
			@RequestParam(name="size",defaultValue = "5")int size,
			@RequestParam(name="motCle",defaultValue = "")String motCle
			
			)
	{
		/*
		 * recuperer tous les data sans paramettre de rechercher
		 */
		//Page<Patient>pagePatients = patientRepository.findAll(PageRequest.of(page, size));
		Page<Patient>pagePatients = patientRepository.findByNomContains(motCle,PageRequest.of(page, size));

		
		model.addAttribute("listPatients",pagePatients.getContent());
		model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
		model.addAttribute("currentPage",page);
		model.addAttribute("currentMotCle",motCle);

		return "patients";
	}
	/*
	 * pas obliger de mettre request param ,à condition que le
	 * nom de la variable soit identique au nom de IHM
	 */
	@GetMapping(path = "/delete")
	public String delete( Long id,int page,String motCle
			)
	{
		patientRepository.deleteById(id);
		return "redirect:/index?page="+page+"&motCle="+motCle;
	}

	/*
	 * Comme nous somme dans @Controller et faire comprendre
	 * a dispacherServlet qu'il ne s'agit pas d'une vu
	 * on doit ajouter @ResponsBody (utiliser par default dans
	 * @RestController en retournant le resultat dans le corps de
	 * la requete.)
	 */
	@GetMapping(path = "/patients")
	@ResponseBody
	public List<Patient> patients()
	{
		return patientRepository.findAll();
	}
	
	@GetMapping(path = "/")
	public String pageAccueil()
	{
		return "redirect:/index";
	}
	@GetMapping(path = "/formPatient")
	public String formPatient(Model model)
	{
		model.addAttribute("patient", new Patient());
		return "formPatient";
	}
	
	@PostMapping(path = "/enregistrerPatient")
	public String enregistrerPatient(Model model,@Valid Patient patient,BindingResult bindingResult,
			@RequestParam(name="page",defaultValue = "0") int page,
			@RequestParam(name="motCle",defaultValue = "")String motCle)
	{
		System.out.println("page:"+page);
		System.out.println("motCle:"+motCle);
		if(bindingResult.hasErrors())
		{
			return "formPatient"; 
		}
		patientRepository.save(patient);
		
		return "redirect:/index?page="+page+"&motCle="+motCle;
		//return "redirect:/index";
	}
	@GetMapping(path = "/editpatient")
	public String edit(Model model, Long id,

			@RequestParam(name="page",defaultValue = "0") int page,
			@RequestParam(name="motCle",defaultValue = "")String motCle)
	{
		
		Patient patient=patientRepository.findById(id).orElse(null);
		if(patient == null)
		{
			throw new RuntimeException("Patient introuvable");
		}
		model.addAttribute("patient", patient);
		model.addAttribute("currentPage",page);
		model.addAttribute("currentMotCle",motCle);
		
		return "editPatient";
	}
}
