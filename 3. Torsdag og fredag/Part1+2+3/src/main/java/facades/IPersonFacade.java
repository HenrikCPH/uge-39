package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;

public interface IPersonFacade {
    PersonDTO addPerson(String firstName, String lastName, String phone, String street, String zip, String city) throws MissingInputException;
    PersonDTO deletePerson(int id) throws PersonNotFoundException;
    PersonDTO getPerson(int id) throws PersonNotFoundException;
    PersonsDTO getAllPersons();
    PersonDTO editPerson(PersonDTO dto) throws PersonNotFoundException, MissingInputException;
}