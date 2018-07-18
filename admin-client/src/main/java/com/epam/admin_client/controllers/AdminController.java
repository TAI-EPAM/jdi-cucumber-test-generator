package com.epam.admin_client.controllers;

import com.epam.admin_client.service.JiraService;
import com.epam.admin_client.service.ProjectService;
import com.epam.admin_client.service.UserService;
import com.epam.test_generator.controllers.admin.request.JiraSettingsCreateDTO;
import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.controllers.admin.response.JiraSettingsDTO;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private UserService userService;
    private ProjectService projectService;
    private JiraService jiraService;

    public AdminController(UserService userService,
                           ProjectService projectService,
                           JiraService jiraService) {
        this.userService = userService;
        this.projectService = projectService;
        this.jiraService = jiraService;
    }

    @RequestMapping("/")
    public String index(){
        return "redirect:getUsers";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model){
        model.addAttribute("error", error);
        return "login";
    }
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    /**
     * get list of all users
     */
    @RequestMapping("/getUsers")
    public String getUsers(@RequestParam(value = "error", required = false) String error,
                           Model model, Authentication auth) throws IOException {
        model.addAttribute("error", error);
        UserDTO[] users = userService.getAllUsers((String) auth.getCredentials());
        model.addAttribute("userList", users);
        return "getUsers";
    }

    /**
     * get list of all projects
     */
    @GetMapping("/getProjects")
    public String getProjects(@RequestParam(value = "error", required = false) String error,
                              Model model, Authentication auth) throws IOException {
        model.addAttribute("error", error);
        ProjectDTO[] projects = projectService.getAllProject((String) auth.getCredentials());
        model.addAttribute("projectList", projects);
        return "getProjects";
    }

    /**
     * getting jira settings
     */
    @GetMapping("/getJira")
    public String getJiraSettings(@RequestParam(value = "error", required = false) String error,
                                  Model model, Authentication auth) throws IOException {
        model.addAttribute("error", error);
        JiraSettingsDTO[] settings = jiraService.getJiraSettings((String) auth.getCredentials());
        model.addAttribute("jiraSettings", settings);
        return "getJira";
    }

    /**
     * Update jira settings
     */
    @PostMapping("/setJiraSettings")
    public String setJiraSettings(@ModelAttribute("jiraDTO") JiraSettingsCreateDTO jiraDTO,
                                  Authentication auth) throws JsonProcessingException {
        jiraService.setJiraSettings(jiraDTO, (String) auth.getCredentials());
        return "getJira";
    }

    /**
     * Set new role to user
     */
    @PostMapping("/setRole")
    public String setRole(@ModelAttribute("userDto") UserRoleUpdateDTO userDTO,
                          Authentication auth) throws JsonProcessingException {
        userService.setUserRole(userDTO, (String) auth.getCredentials());
        return "redirect:getUsers";
    }

    @PostMapping("/blockUser")
    public String blockUser(@ModelAttribute("id") String userId,
                                Authentication auth) throws JsonProcessingException {
        userService.blockUser(userId,(String)auth.getCredentials());
        return "redirect:getUsers";
    }

    @PostMapping("/unblockUser")
    public String unblockUser(@ModelAttribute("id") String userId,
                                Authentication auth) throws JsonProcessingException {
        userService.unblockUser(userId,(String)auth.getCredentials());
        return "redirect:getUsers";
    }

    /**
     * delete project by id
     */
    @PostMapping("/deleteProject")
    public String deleteProject(@ModelAttribute("projectDTO") ProjectDTO projectDTO,
                                Authentication auth) {
        projectService.deleteProject(projectDTO, (String) auth.getCredentials());
        return "redirect:getProjects";
    }

    /**
     * Creating project
     *
     */

    @PostMapping("/createProject")
    public String createProject(@ModelAttribute("ProjectCreateDTO") ProjectCreateDTO projectDTO,
                                Authentication auth) throws JsonProcessingException {
        projectService.createProject(projectDTO, (String) auth.getCredentials());
        return "redirect:getProjects";
    }
}

