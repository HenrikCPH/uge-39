package facades;

import entities.Person;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            return new PersonsDTO(em.createNamedQuery("Person.getAllRows", Person.class).getResultList());
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException, MissingInputException {
        if ((p.getFirstName().length() == 0) || (p.getLastName().length() == 0)) {
            throw new MissingInputException("First name and/or Last Name is missing");
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            Person person = em.find(Person.class, p.getId());
            if (person == null) {
                throw new PersonNotFoundException(String.format("Person with id: (%d) not found", p.getId()));
            } else {
                person.setFirstName(p.getFirstName());
                person.setLastName(p.getLastName());
                person.setPhone(p.getPhone());
                person.setLastEdited(p.getCreated());
                //person.setLastEdited();
                person.getAddress().setStreet(p.getStreet());
                person.getAddress().setZip(p.getZip());
                person.getAddress().setCity(p.getCity());
            }
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException(String.format("Person with id: (%d) not found", id));
        } else {
            try {
                em.getTransaction().begin();
                em.remove(person);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new PersonDTO(person);
        }
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person == null) {
                throw new PersonNotFoundException(String.format("Person with id: (%d) not found", id));
            } else {
                return new PersonDTO(person);
            }
        } finally {
            em.close();
        }

    }

    @Override
        public PersonDTO addPerson(String firstName, String lastName, String phone, String street, String zip, String city) throws MissingInputException{
	if((firstName.length() == 0) || (lastName.length() == 0)){
		throw new MissingInputException("First Name and/pr Last name is missing");
	}
	EntityManager em = getEntityManager();
	Person person = new Person(firstName, lastName, phone);

	try {
		em.getTransaction().begin();
			Query query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street AND a.zip = :zip AND a.city = :ctiy");
			query.setParameter("street", street);
			query.setParameter("zip",zip);
			query.setParameter("city",city);
			List<Address> addresses = query.getResultList();
			if(addresses.size() > 0){
		           person.setAddress(addresses.get(0));
			} else {
			   person.setAddress(new Address(street,zip,city));
			}
			em.persist(person);
		      em.getTransaction().commit();
		} finally {
			em.close();
		}
		return new PersonDTO(person);
	}
        
}
