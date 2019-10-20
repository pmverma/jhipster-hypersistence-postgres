package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ESeven.
 */
@Entity
@Table(name = "eseven")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ESeven implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "seven")
    private String seven;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeven() {
        return seven;
    }

    public ESeven seven(String seven) {
        this.seven = seven;
        return this;
    }

    public void setSeven(String seven) {
        this.seven = seven;
    }

    public User getUser() {
        return user;
    }

    public ESeven user(User user) {
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
        if (!(o instanceof ESeven)) {
            return false;
        }
        return id != null && id.equals(((ESeven) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ESeven{" +
            "id=" + getId() +
            ", seven='" + getSeven() + "'" +
            "}";
    }
}
