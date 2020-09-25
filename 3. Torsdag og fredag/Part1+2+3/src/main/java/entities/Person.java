package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;


@Entity
@NamedQueries({
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person"),
@NamedQuery(name = "Person.getAllRows", query = "SELECT p from Person p")})

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastEdited;
    private String firstName;
    private String lastName;
    private String phone;      
    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;
    
    public Person() {
    }  

    public Person(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Person(int id, Date created, Date lastEdited, String firstName, String lastName, String phone) {
        this.id = id;
        this.created = created;
        this.lastEdited = lastEdited;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address){
        if(address != null){
            this.address = address;
            address.addPerson(this);
        } else {
            this.address = null;
        }
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
   
}
