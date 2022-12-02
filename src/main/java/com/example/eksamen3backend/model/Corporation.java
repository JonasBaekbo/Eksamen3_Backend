package com.example.eksamen3backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "corporation")
public class Corporation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Corporation_name")
    private String name;

    @Column(name = "corporation_address")
    private String address;

    @Column(name = "corporation_logo")
    private String logo;

    @Column(name ="corporation_city")
    private String city;

    @Column(name = "corporation_country")
    private String country;

    @OneToMany(mappedBy = "corporation")
    @JsonBackReference("conversation")
    private Set<Conversation> conversations = new HashSet<>();


    @OneToMany
    @JsonBackReference("employments")
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "corporation_id", referencedColumnName = "id")
    @Where(clause = "moved_from_corporation is null")
    private Set<Employment> employments = new HashSet<>();

    @JsonManagedReference
    public Set<Employment> currentEmployments() {
        return employments;
    }
}
