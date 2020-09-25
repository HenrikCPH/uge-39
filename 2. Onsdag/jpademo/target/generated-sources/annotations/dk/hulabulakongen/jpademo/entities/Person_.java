package dk.hulabulakongen.jpademo.entities;

import dk.hulabulakongen.jpademo.entities.Address;
import dk.hulabulakongen.jpademo.entities.Fee;
import dk.hulabulakongen.jpademo.entities.SwimStyle;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-09-23T13:38:08")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile ListAttribute<Person, Fee> fees;
    public static volatile SingularAttribute<Person, Address> address;
    public static volatile SingularAttribute<Person, Integer> year;
    public static volatile SingularAttribute<Person, String> name;
    public static volatile ListAttribute<Person, SwimStyle> styles;
    public static volatile SingularAttribute<Person, Long> p_id;

}