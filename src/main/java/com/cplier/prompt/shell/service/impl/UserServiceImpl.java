package com.cplier.prompt.shell.service.impl;

import com.cplier.prompt.shell.common.ProgressUpdateEvent;
import com.cplier.prompt.shell.model.CliUser;
import com.cplier.prompt.shell.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Service
public class UserServiceImpl extends Observable implements UserService {

  @Resource Observer observer;
  private static List<CliUser> users = new ArrayList<>();

  static {
    synchronized (UserServiceImpl.class) {
      ClassPathResource cpr = new ClassPathResource("cli-users.json");
      try {
        ObjectMapper mapper = new ObjectMapper();
        users = mapper.readValue(cpr.getInputStream(), new TypeReference<List<CliUser>>() {});
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public CliUser findById(Long id) {
    for (CliUser user : users) {
      if (id.equals(user.getId())) {
        return user;
      }
    }
    return null;
  }

  @Override
  public CliUser findByUsername(String username) {
    for (CliUser user : users) {
      if (username.equals(user.getUsername())) {
        return user;
      }
    }
    return null;
  }

  @Override
  public List<CliUser> findAll() {
    return users;
  }

  @Override
  public boolean exists(String username) {
    for (CliUser user : users) {
      if (username.equals(user.getUsername())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public CliUser create(CliUser user) {
    user.setId(getNextId());
    users.add(user);
    return user;
  }

  @Override
  public CliUser update(CliUser user) {
    for (CliUser u : users) {
      if (u.getId().equals(user.getId())) {
        u = user;
        return user;
      }
    }
    throw new IllegalArgumentException("No matching user found!");
  }

  @Override
  public long updateAll() {
    long numberOfUsers = 2000;
    for (long i = 1; i <= numberOfUsers; i++) {
      // do some operation ...
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // notify observer of the change
      if (observer != null) {
        String message = "";
        if (i < numberOfUsers) {
          message = ":: please WAIT update operation in progress";
        }
        observer.update(this, new ProgressUpdateEvent(i, numberOfUsers, message));
      }
    }
    return numberOfUsers;
  }

  private long getNextId() {
    long maxId = 0;
    for (CliUser user : users) {
      if (user.getId() > maxId) {
        maxId = user.getId();
      }
    }
    return maxId + 1;
  }
}
