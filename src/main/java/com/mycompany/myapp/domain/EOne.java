package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A EOne.
 */
@Entity
@Table(name = "e_one")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EOne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "one", nullable = false, unique = true)
    private String one;

    @OneToMany(mappedBy = "eOne")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ETwo> eTwoOS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOne() {
        return one;
    }

    public EOne one(String one) {
        this.one = one;
        return this;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public Set<ETwo> getETwoOS() {
        return eTwoOS;
    }

    public EOne eTwoOS(Set<ETwo> eTwos) {
        this.eTwoOS = eTwos;
        return this;
    }

    public EOne addETwoO(ETwo eTwo) {
        this.eTwoOS.add(eTwo);
        eTwo.setEOne(this);
        return this;
    }

    public EOne removeETwoO(ETwo eTwo) {
        this.eTwoOS.remove(eTwo);
        eTwo.setEOne(null);
        return this;
    }

    public void setETwoOS(Set<ETwo> eTwos) {
        this.eTwoOS = eTwos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EOne)) {
            return false;
        }
        return id != null && id.equals(((EOne) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EOne{" +
            "id=" + getId() +
            ", one='" + getOne() + "'" +
            "}";
    }
}
