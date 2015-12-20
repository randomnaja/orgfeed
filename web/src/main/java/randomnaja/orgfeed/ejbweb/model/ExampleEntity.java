package randomnaja.orgfeed.ejbweb.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="EXAMPLE")
public class ExampleEntity {

	@Id
    private Long id;
}
