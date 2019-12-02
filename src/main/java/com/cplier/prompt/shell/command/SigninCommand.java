package com.cplier.prompt.shell.command;

import com.cplier.prompt.shell.common.InputReader;
import com.cplier.prompt.shell.common.ShellHelper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@ShellComponent
public class SigninCommand extends SecuredCommand {

  @Lazy @Resource ShellHelper shellHelper;

  @Lazy @Resource InputReader inputReader;

  @Resource AuthenticationManager authenticationManager;

  @ShellMethod("Sign in as clidemo user")
  public void signin() {
    String username;
    boolean usernameInvalid = true;
    do {
      username = inputReader.prompt("Please enter your username");
      if (StringUtils.hasText(username)) {
        usernameInvalid = false;
      } else {
        shellHelper.printWarning("Username can not be empty string!");
      }
    } while (usernameInvalid);
    String password = inputReader.prompt("Please enter your password", null, false);
    Authentication request = new UsernamePasswordAuthenticationToken(username, password);

    try {
      Authentication result = authenticationManager.authenticate(request);
      SecurityContextHolder.getContext().setAuthentication(result);
      shellHelper.printSuccess(
          "Credentials successfully authenticated! " + username + " -> welcome to CliDemo.");
    } catch (AuthenticationException e) {
      shellHelper.printWarning("Authentication failed: " + e.getMessage());
    }
  }
}
