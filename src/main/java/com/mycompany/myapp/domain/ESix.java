package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ESix.
 */
@Entity
@Table(name = "e_six")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ESix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "six", nullable = false)
    private String six;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSix() {
        return six;
    }

    public ESix six(String six) {
        this.six = six;
        return this;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public User getUser() {
        return user;
    }

    public ESix user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ESix)) {
            return false;
        }
        return id != null && id.equals(((ESix) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ESix{" +
            "id=" + getId() +
            ", six='" + getSix() + "'" +
            "}";
    }
}
