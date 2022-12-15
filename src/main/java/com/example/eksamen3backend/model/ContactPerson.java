package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "contact_person")
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "contactperson_name")
    private String name;

    @Column(name="contactperson_status")
    private int isActive;

    @OneToOne
    private Photo CPimage;

    @OneToMany(mappedBy = "contactPerson")
    @JsonBackReference("contactPersonConversation")
    private Set<Conversation> conversations = new HashSet<>();


    public ContactPerson() {
        super();
    }

    @OneToMany
    @JsonBackReference("employment")
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "contact_person_id", referencedColumnName = "id")
    @Where(clause = "moved_from_corporation is null or moved_from_corporation > now()")
    private Set<Employment> employments = new HashSet<>();

    @JsonManagedReference
    public Set<Employment> currentEmployments() {
        return employments;
    }

    @OneToMany
    @JsonBackReference("employmentHistory")
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "contact_person_id", referencedColumnName = "id")
    @OrderBy("movedFromCorporation DESC")
    private Set<Employment> employmentHistory1 = new HashSet<>();

    @JsonManagedReference
    public Set<Employment> employmentHistory() {
        return employmentHistory1;
    }

}
