package com.mycompany.myapp.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.EThree} entity.
 */
public class EThreeDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant three;


    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getThree() {
        return three;
    }

    public void setThree(Instant three) {
        this.three = three;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EThreeDTO eThreeDTO = (EThreeDTO) o;
        if (eThreeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eThreeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EThreeDTO{" +
            "id=" + getId() +
            ", three='" + getThree() + "'" +
            ", user=" + getUserId() +
            "}";
    }
}
