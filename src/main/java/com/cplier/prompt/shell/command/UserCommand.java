package com.cplier.prompt.shell.command;

import com.cplier.prompt.shell.common.Gender;
import com.cplier.prompt.shell.common.InputReader;
import com.cplier.prompt.shell.common.ShellHelper;
import com.cplier.prompt.shell.common.table.BeanTableModelBuilder;
import com.cplier.prompt.shell.model.CliUser;
import com.cplier.prompt.shell.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.*;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@ShellComponent
public class UserCommand extends SecuredCommand{

  @Resource ShellHelper shellHelper;
  @Resource InputReader inputReader;
  @Resource UserService userService;
  @Resource ObjectMapper objectMapper;

  @ShellMethod("Create new user with supplied username")
  public void createUser(@ShellOption({"-U", "--username"}) String username) {
    if (userService.exists(username)) {
      shellHelper.printError(
          String.format("User with username='%s' already exists --> ABORTING", username));
      return;
    }

    CliUser user = new CliUser();
    user.setUsername(username);

    shellHelper.printInfo("Please enter new user data:");
    // 1. read user's fullName -------------------------------------------
    do {
      String fullName = inputReader.prompt("Full name");
      if (StringUtils.hasText(fullName)) {
        user.setFullName(fullName);
      } else {
        shellHelper.printWarning(
            "User's full name CAN NOT be empty string? Please ener valid value!");
      }
    } while (user.getFullName() == null);

    // 2. read user's password --------------------------------------------
    do {
      String password = inputReader.prompt("Password", "secret", false);
      if (StringUtils.hasText(password)) {
        user.setPassword(password);
      } else {
        shellHelper.printWarning("Password'CAN NOT be empty string? Please enter valid value!");
      }
    } while (user.getPassword() == null);

    // 3. read user's Gender ----------------------------------------------
    Map<String, String> options = new HashMap<>();
    options.put("M", Gender.MALE.name());
    options.put("F", Gender.FEMALE.name());
    options.put("D", Gender.DIVERSE.name());

    String genderValue =
        inputReader.selectFromList(
            "Gender", "Please enter one of the [] values", options, true, null);
    Gender gender = Gender.valueOf(options.get(genderValue.toUpperCase()));
    user.setGender(gender);

    // 4. Prompt for superuser attribute ------------------------------
    String superuserValue =
        inputReader.promptWithOptions("New user is superuser", "N", Arrays.asList("Y", "N"));
    if ("Y".equals(superuserValue)) {
      user.setSuperuser(true);
    } else {
      user.setSuperuser(false);
    }

    // Print user's input -------------------------------------------------
    shellHelper.printInfo("\nCreating new user:");
    shellHelper.print("\nUsername: " + user.getUsername());
    shellHelper.print("Password: " + user.getPassword());
    shellHelper.print("Fullname: " + user.getFullName());
    shellHelper.print("Gender: " + user.getGender());
    shellHelper.print("Superuser: " + user.isSuperuser() + "\n");

    CliUser createdUser = userService.create(user);
    shellHelper.printSuccess("Created user with id=" + createdUser.getId());
  }

  @ShellMethod("Display list of users")
  public void userList() {
    List<CliUser> users = userService.findAll();

    LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
    headers.put("id", "Id");
    headers.put("username", "Username");
    headers.put("fullName", "Full name");
    headers.put("gender", "Gender");
    headers.put("superuser", "Superuser");
    TableModel model = new BeanListTableModel<>(users, headers);

    TableBuilder tableBuilder = new TableBuilder(model);
    tableBuilder.addInnerBorder(BorderStyle.fancy_light);
    tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
    shellHelper.print(tableBuilder.build().render(80));
  }

  @ShellMethod("Update and synchronize all users in local database with external source")
  public void updateAllUsers() {
    shellHelper.printInfo("Starting local user db update");
    long numOfUsers = userService.updateAll();
    String successMessage = shellHelper.getSuccessMessage("SUCCESS >>");
    successMessage =
        successMessage + String.format(" Total of %d local db users updated!", numOfUsers);
    shellHelper.print(successMessage);
  }

  @ShellMethod("Display details of user with supplied username")
  public void userDetails(@ShellOption({"-U", "--username"}) String username) {
    CliUser user = userService.findByUsername(username);
    if (user == null) {
      shellHelper.printWarning("No user with the supplied username could be found?!");
      return;
    }
    displayUser(user);
  }

  private void displayUser(CliUser user) {
    LinkedHashMap<String, Object> labels = new LinkedHashMap<>();
    labels.put("id", "Id");
    labels.put("username", "Username");
    labels.put("fullName", "Full name");
    labels.put("gender", "Gender");
    labels.put("superuser", "Superuser");
    labels.put("password", "Password");

    String[] header = new String[] {"Property", "Value"};
    BeanTableModelBuilder builder = new BeanTableModelBuilder(user, objectMapper);
    TableModel model = builder.withLabels(labels).withHeader(header).build();

    TableBuilder tableBuilder = new TableBuilder(model);

    tableBuilder.addInnerBorder(BorderStyle.fancy_light);
    tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
    tableBuilder.on(CellMatchers.column(0)).addSizer(new AbsoluteWidthSizeConstraints(20));
    tableBuilder.on(CellMatchers.column(1)).addSizer(new AbsoluteWidthSizeConstraints(30));
    shellHelper.print(tableBuilder.build().render(80));
  }
}
