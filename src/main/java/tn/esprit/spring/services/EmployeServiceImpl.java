package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;
	private static final Logger logger = Logger.getLogger(EmployeServiceImpl.class);

	public int ajouterEmploye(Employe employe) {
		try {
			logger.info("Start the method / INFO");
			logger.debug("Start the method / DEBUG");
			employeRepository.save(employe);
			logger.info("Finishing the method with sucess / INFO");
			logger.debug("Finishing the method with sucess / DEBUG");
		} catch (Exception e) {
			logger.error("Method failed /ERROR " + e);
		}
		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		try {
			logger.info("Start the method / INFO");
			logger.debug("Start the method / DEBUG");
			Optional<Employe> employe = employeRepository.findById(employeId);
			Employe e = new Employe();
			if (employe.isPresent()) {
				e = employe.get();
				e.setEmail(email);
				employeRepository.save(e);
			}
			logger.info("Finishing the method with sucess / INFO");
			logger.debug("Finishing the method with sucess / DEBUG");
		} catch (Exception e) {
			logger.error("Method failed /ERROR " + e);
		}

	}

	@Transactional
	public void affecterEmployeADepartement(int employeId, int depId) {
		try {
			logger.info("Start the method / INFO");
			logger.debug("Start the method / INFO");

			Optional<Departement> depManagedEntity = deptRepoistory.findById(depId);
			Departement d = new Departement();
			if (depManagedEntity.isPresent()) {
				d = depManagedEntity.get();

			}
			Optional<Employe> employeManagedEntity = employeRepository.findById(employeId);
			Employe e = new Employe();
			if (employeManagedEntity.isPresent()) {
				e = employeManagedEntity.get();
			}
			if (d.getEmployes() == null) {
				List<Employe> employes = new ArrayList<>();
				employes.add(e);
				d.setEmployes(employes);
				logger.info("Finishing the method with sucess / INFO");
				logger.debug("Finishing the method with sucess / DEBUG");
			} else {

				d.getEmployes().add(e);
				logger.info("Finishing the method with sucess / INFO");
				logger.debug("Finishing the method with sucess / DEBUG");

			}
		} catch (Exception e) {
			logger.error("Method failed /ERROR" + e);

		}

	}

	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId) {
		try {
			logger.info("Start the method / INFO");
			logger.debug("Start the method / INFO");
			Optional<Departement> depManagedEntity = deptRepoistory.findById(depId);
			Departement d = new Departement();
			if (depManagedEntity.isPresent()) {
				d = depManagedEntity.get();

			}
			int employeNb = d.getEmployes().size();
			for (int index = 0; index < employeNb; index++) {
				if (d.getEmployes().get(index).getId() == employeId) {
					d.getEmployes().remove(index);
					logger.info("Finishing the method with sucess / INFO");
					logger.debug("Finishing the method with sucess / DEBUG");
					break;// a revoir
				}
			}
		} catch (Exception e) {
			logger.error("Method failed /ERROR" + e);
		}
	}

	public int ajouterContrat(Contrat contrat) {
		try {
			logger.info("Start the method / INFO");
			logger.debug("Start the method / INFO");
			contratRepoistory.save(contrat);
			logger.info("Finishing the method with sucess / INFO");
			logger.debug("Finishing the method with sucess / DEBUG");
		} catch (Exception e) {
			logger.error("Method failed /ERROR" + e);
		}
		return contrat.getReference();

	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		try {
			logger.info("Finishing the method with sucess / INFO");
			logger.debug("Finishing the method with sucess / DEBUG");
			Optional<Contrat> contratManagedEntity = contratRepoistory.findById(contratId);
			Contrat c = new Contrat();
			if (contratManagedEntity.isPresent()) {
				c = contratManagedEntity.get();
			}
			Optional<Employe> employe = employeRepository.findById(employeId);
			Employe e = new Employe();
			if (employe.isPresent()) {
				e = employe.get();
			}

			c.setEmploye(e);
			contratRepoistory.save(c);
			logger.info("Finishing the method with sucess / INFO");
			logger.debug("Finishing the method with sucess / DEBUG");
		} catch (Exception e) {
			logger.error("Method failed /ERROR" + e);

		}
	}

	public String getEmployePrenomById(int employeId) {
		Optional<Employe> employe = employeRepository.findById(employeId);
		Employe e = new Employe();
		if (employe.isPresent()) {
			e = employe.get();
		}
		return e.getPrenom();
	}

	public void deleteEmployeById(int employeId) {
		Optional<Employe> employe = employeRepository.findById(employeId);
		Employe e = new Employe();
		if (employe.isPresent()) {
			e = employe.get();
			// Desaffecter l'employe de tous les departements
			// c'est le bout master qui permet de mettre a jour
			// la table d'association
			for (Departement dep : e.getDepartements()) {
				dep.getEmployes().remove(e);
			}
			employeRepository.delete(e);
		}
	}

	public void deleteContratById(int contratId) {
		Optional<Contrat> contratManagedEntity = contratRepoistory.findById(contratId);
		Contrat c = new Contrat();
		if (contratManagedEntity.isPresent()) {
			c = contratManagedEntity.get();
		}
		contratRepoistory.delete(c);

	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}

	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}

	public void deleteAllContratJPQL() {
		employeRepository.deleteAllContratJPQL();
	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		return (List<Employe>) employeRepository.findAll();
	}

}