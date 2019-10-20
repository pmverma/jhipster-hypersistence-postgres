package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A EThree.
 */
@Entity
@Table(name = "e_three")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EThree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "three", nullable = false)
    private Instant three;

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

    public Instant getThree() {
        return three;
    }

    public EThree three(Instant three) {
        this.three = three;
        return this;
    }

    public void setThree(Instant three) {
        this.three = three;
    }

    public User getUser() {
        return user;
    }

    public EThree user(User user) {
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
        if (!(o instanceof EThree)) {
            return false;
        }
        return id != null && id.equals(((EThree) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EThree{" +
            "id=" + getId() +
            ", three='" + getThree() + "'" +
            "}";
    }
}
