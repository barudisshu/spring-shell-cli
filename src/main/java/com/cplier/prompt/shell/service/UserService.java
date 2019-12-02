package com.cplier.prompt.shell.service;

import com.cplier.prompt.shell.model.CliUser;

import java.util.List;

public interface UserService {

  CliUser findById(Long id);

  CliUser findByUsername(String username);

  List<CliUser> findAll();

  boolean exists(String username);

  CliUser create(CliUser user);

  CliUser update(CliUser user);

  long updateAll();
}
