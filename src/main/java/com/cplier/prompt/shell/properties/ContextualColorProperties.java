package com.cplier.prompt.shell.properties;

import com.cplier.prompt.shell.common.PromptColor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shell.out")
public class ContextualColorProperties {

  private String info;
  private String success;
  private String warning;
  private String error;

  public PromptColor getInfo() {
    return PromptColor.valueOf(info);
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public PromptColor getSuccess() {
    return PromptColor.valueOf(success);
  }

  public void setSuccess(String success) {
    this.success = success;
  }

  public PromptColor getWarning() {
    return PromptColor.valueOf(warning);
  }

  public void setWarning(String warning) {
    this.warning = warning;
  }

  public PromptColor getError() {
    return PromptColor.valueOf(error);
  }

  public void setError(String error) {
    this.error = error;
  }
}
