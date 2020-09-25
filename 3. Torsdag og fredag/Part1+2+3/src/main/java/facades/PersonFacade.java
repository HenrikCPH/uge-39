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
import utils.EMF_Creator;

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
    public PersonDTO addPerson(String firstName, String lastName, String phone, String street, String zip, String city) throws MissingInputException {
        if ((firstName.length() == 0) || (lastName.length() == 0)) {
            throw new MissingInputException("First Name and/pr Last name is missing");
        }
        EntityManager em = getEntityManager();
        Person person = new Person(firstName, lastName, phone);

        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street AND a.zip = :zip AND a.city = :ctiy");
            query.setParameter("street", street);
            query.setParameter("zip", zip);
            query.setParameter("city", city);
            List<Address> addresses = query.getResultList();
            if (addresses.size() > 0) {
                person.setAddress(addresses.get(0));
            } else {
                person.setAddress(new Address(street, zip, city));
            }
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    public static void main(String[] args) {
        //Create emf pointing to the dev-database
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("Mark Sørensen", "cph-ms845", "Tenet");
        Person p2 = new Person("Henrik Lønquist Thomasen", "cph-ht92", "1917");
        Person p3 = new Person("Yones El Bana", "cph-ye7", "parasite");

        Address a1 = new Address("Big Street", "This zippo lighter", "censur city");
        Address a2 = new Address("Small Street", "Another zippo lighter", "spam town");
        Address a3 = new Address("Tiny Street", "missing zip", "no city exist");

        p1.setAddress(a2);
        p2.setAddress(a1);
        p3.setAddress(a3);
        
        try {
            em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
