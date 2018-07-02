package com.epam.admin_client.controllers;


import com.epam.admin_client.service.exception.JiraException;
import com.epam.admin_client.service.exception.ProjectException;
import com.epam.admin_client.service.exception.UserException;
import com.epam.admin_client.service.exception.UserRoleExeption;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GeneralExceptionController {

    @ExceptionHandler(HttpClientErrorException.class)
    public String httpServerExceptionHandler(HttpClientErrorException e) {
        return "redirect:getProjects?error=Server+not+response";
    }

    @ExceptionHandler(UserException.class)
    public String userExceptionHandler(UserException e) {
        return "redirect:getUsers?error=User+not+found";
    }

    @ExceptionHandler(UserRoleExeption.class)
    public String userROleExceptionHandler(UserRoleExeption e) {
        return "redirect:getUsers?error=User+not+found+or+role+doesn't+exist";
    }

    @ExceptionHandler(ProjectException.class)
    public String projectExceptionHandler(ProjectException e) {
        return "redirect:getProjects?error=Project+Doesn't+Exist";
    }

    @ExceptionHandler(JiraException.class)
    public String jiraExceptionHandler(JiraException e) {
        return "redirect:getJira?error=Wrong+Data";
    }
}
