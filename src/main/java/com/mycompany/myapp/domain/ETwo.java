package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ETwo.
 */
@Entity
@Table(name = "e_two")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ETwo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "two", nullable = false)
    private Integer two;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("eTwos")
    private EOne eTwoT;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("eTwoOS")
    private EOne eOne;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTwo() {
        return two;
    }

    public ETwo two(Integer two) {
        this.two = two;
        return this;
    }

    public void setTwo(Integer two) {
        this.two = two;
    }

    public EOne getETwoT() {
        return eTwoT;
    }

    public ETwo eTwoT(EOne eOne) {
        this.eTwoT = eOne;
        return this;
    }

    public void setETwoT(EOne eOne) {
        this.eTwoT = eOne;
    }

    public EOne getEOne() {
        return eOne;
    }

    public ETwo eOne(EOne eOne) {
        this.eOne = eOne;
        return this;
    }

    public void setEOne(EOne eOne) {
        this.eOne = eOne;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ETwo)) {
            return false;
        }
        return id != null && id.equals(((ETwo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ETwo{" +
            "id=" + getId() +
            ", two=" + getTwo() +
            "}";
    }
}
