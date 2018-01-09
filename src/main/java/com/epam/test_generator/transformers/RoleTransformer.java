package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.RoleDTO;
import com.epam.test_generator.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleTransformer extends AbstractDozerTransformer<Role, RoleDTO> {

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    protected Class<RoleDTO> getDTOClass() {
        return RoleDTO.class;
    }

}

