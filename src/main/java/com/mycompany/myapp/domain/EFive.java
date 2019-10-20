package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A EFive.
 */
@Entity
@Table(name = "e_five")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EFive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "five", nullable = false)
    private String five;

    @ManyToMany(mappedBy = "eFives")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<EFour> eFours = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFive() {
        return five;
    }

    public EFive five(String five) {
        this.five = five;
        return this;
    }

    public void setFive(String five) {
        this.five = five;
    }

    public Set<EFour> getEFours() {
        return eFours;
    }

    public EFive eFours(Set<EFour> eFours) {
        this.eFours = eFours;
        return this;
    }

    public EFive addEFour(EFour eFour) {
        this.eFours.add(eFour);
        eFour.getEFives().add(this);
        return this;
    }

    public EFive removeEFour(EFour eFour) {
        this.eFours.remove(eFour);
        eFour.getEFives().remove(this);
        return this;
    }

    public void setEFours(Set<EFour> eFours) {
        this.eFours = eFours;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EFive)) {
            return false;
        }
        return id != null && id.equals(((EFive) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EFive{" +
            "id=" + getId() +
            ", five='" + getFive() + "'" +
            "}";
    }
}
