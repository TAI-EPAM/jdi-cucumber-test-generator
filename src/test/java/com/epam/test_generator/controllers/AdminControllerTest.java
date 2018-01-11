package com.epam.test_generator.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.test_generator.dto.ChangeUserRoleDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.AdminService;
import com.epam.test_generator.services.RoleService;
import com.epam.test_generator.services.TokenService;
import com.epam.test_generator.services.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    private MockMvc mockMvc;
    private ChangeUserRoleDTO userChangeRole;
    private ObjectMapper mapper;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        userChangeRole = new ChangeUserRoleDTO();
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
    }

    @Test
    public void changeUserRole_SimpleRole_Status200() throws Exception {
        userChangeRole.setEmail("email@mail.com");
        userChangeRole.setRole("Role");
        final String json = mapper.writeValueAsString(userChangeRole);

        mockMvc.perform(put("/admin/changeroles").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void changeUSerRole__InvalidRole_Status500() throws Exception{

    }
}