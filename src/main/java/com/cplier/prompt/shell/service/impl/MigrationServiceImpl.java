package com.cplier.prompt.shell.service.impl;

import com.cplier.prompt.shell.common.ProgressUpdateEvent;
import com.cplier.prompt.shell.service.MigrationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Observable;
import java.util.Observer;

@Service
public class MigrationServiceImpl extends Observable implements MigrationService {

  @Resource Observer observer;

  @Override
  public long diffAllServices() {
    long numberOfServices = 2000;
    for (long i = 1; i <= numberOfServices; i++) {
      // do some operation ...
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // notify observer of the change
      if (observer != null) {
        String message = "";
        if (i < numberOfServices) {
          message = ":: please WAIT diff services in progress";
        }
        observer.update(this, new ProgressUpdateEvent(i, numberOfServices, message));
      }
    }
    return numberOfServices;
  }

  @Override
  public long diffAllFunctions() {
    long numberOfFunctions = 2000;
    for (long i = 1; i <= numberOfFunctions; i++) {
      // do some operation ...
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // notify observer of the change
      if (observer != null) {
        String message = "";
        if (i < numberOfFunctions) {
          message = ":: please WAIT diff functions in progress";
        }
        observer.update(this, new ProgressUpdateEvent(i, numberOfFunctions, message));
      }
    }
    return numberOfFunctions;
  }
}
