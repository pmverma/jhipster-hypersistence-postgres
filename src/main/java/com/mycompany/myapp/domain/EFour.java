package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A EFour.
 */
@Entity
@Table(name = "e_four")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EFour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Lob
    @Column(name = "four", nullable = false)
    private byte[] four;

    @Column(name = "four_content_type", nullable = false)
    private String fourContentType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "e_four_e_five",
               joinColumns = @JoinColumn(name = "efour_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "e_five_id", referencedColumnName = "id"))
    private Set<EFive> eFives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFour() {
        return four;
    }

    public EFour four(byte[] four) {
        this.four = four;
        return this;
    }

    public void setFour(byte[] four) {
        this.four = four;
    }

    public String getFourContentType() {
        return fourContentType;
    }

    public EFour fourContentType(String fourContentType) {
        this.fourContentType = fourContentType;
        return this;
    }

    public void setFourContentType(String fourContentType) {
        this.fourContentType = fourContentType;
    }

    public Set<EFive> getEFives() {
        return eFives;
    }

    public EFour eFives(Set<EFive> eFives) {
        this.eFives = eFives;
        return this;
    }

    public EFour addEFive(EFive eFive) {
        this.eFives.add(eFive);
        eFive.getEFours().add(this);
        return this;
    }

    public EFour removeEFive(EFive eFive) {
        this.eFives.remove(eFive);
        eFive.getEFours().remove(this);
        return this;
    }

    public void setEFives(Set<EFive> eFives) {
        this.eFives = eFives;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EFour)) {
            return false;
        }
        return id != null && id.equals(((EFour) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EFour{" +
            "id=" + getId() +
            ", four='" + getFour() + "'" +
            ", fourContentType='" + getFourContentType() + "'" +
            "}";
    }
}
