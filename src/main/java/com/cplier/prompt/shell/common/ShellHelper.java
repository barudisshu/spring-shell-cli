package com.cplier.prompt.shell.common;

import com.cplier.prompt.shell.properties.ContextualColorProperties;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import javax.validation.constraints.NotNull;

public class ShellHelper {

  private ContextualColorProperties properties;
  private Terminal terminal;

  public ShellHelper(ContextualColorProperties properties, Terminal terminal) {
    this.properties = properties;
    this.terminal = terminal;
  }

  public String getInfoMessage(String message) {
    return getColored(message, properties.getInfo());
  }

  public String getSuccessMessage(String message) {
    return getColored(message, properties.getSuccess());
  }

  public String getWarningMessage(String message) {
    return getColored(message, properties.getWarning());
  }

  public String getErrorMessage(String message) {
    return getColored(message, properties.getError());
  }

  /**
   * Print message to the console in the default color.
   *
   * @param message message to print
   */
  public void print(String message) {
    print(message, null);
  }

  /**
   * Print message to the console in the success color.
   *
   * @param message message to print
   */
  public void printSuccess(String message) {
    print(message, properties.getSuccess());
  }

  /**
   * Print message to the console in the info color.
   *
   * @param message message to print
   */
  public void printInfo(String message) {
    print(message, properties.getInfo());
  }

  /**
   * Print message to the console in the warning color.
   *
   * @param message message to print
   */
  public void printWarning(String message) {
    print(message, properties.getWarning());
  }

  /**
   * Print message to the console in the error color.
   *
   * @param message message to print
   */
  public void printError(String message) {
    print(message, properties.getError());
  }

  /**
   * Generic Print to the console method.
   *
   * @param message message to print
   * @param color (optional) prompt color
   */
  public void print(String message, PromptColor color) {
    String toPrint = message;
    if (color != null) {
      toPrint = getColored(message, color);
    }
    terminal.writer().println(toPrint);
    terminal.flush();
  }

  private String getColored(@NotNull String message, @NotNull PromptColor color) {
    return (new AttributedStringBuilder())
        .append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle()))
        .toAnsi();
  }

    public Terminal getTerminal() {
        return terminal;
    }
}
