package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EThreeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EThree} and its DTO {@link EThreeDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EThreeMapper extends EntityMapper<EThreeDTO, EThree> {

    @Mapping(source = "user.id", target = "userId")
    EThreeDTO toDto(EThree eThree);

    @Mapping(source = "userId", target = "user")
    EThree toEntity(EThreeDTO eThreeDTO);

    default EThree fromId(Long id) {
        if (id == null) {
            return null;
        }
        EThree eThree = new EThree();
        eThree.setId(id);
        return eThree;
    }
}
