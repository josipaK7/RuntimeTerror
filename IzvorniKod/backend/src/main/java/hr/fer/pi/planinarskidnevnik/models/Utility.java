package hr.fer.pi.planinarskidnevnik.models;

import javax.persistence.*;

@Entity(name = "UTILITY")
public class Utility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "utility_id_gen")
    @SequenceGenerator(name = "utility_id_gen", sequenceName = "utility_id_seq", allocationSize = 1)
    @Column(name = "utility_id")
    private Long id;

    private String name;

    public java.lang.Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }
}
