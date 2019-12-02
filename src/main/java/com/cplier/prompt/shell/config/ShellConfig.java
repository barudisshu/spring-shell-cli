package com.cplier.prompt.shell.config;

import com.cplier.prompt.shell.common.*;
import com.cplier.prompt.shell.properties.ContextualColorProperties;
import com.cplier.prompt.shell.service.impl.ProgressUpdateObserver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.jline.JLineShellAutoConfiguration;

import java.util.Observer;

@Configuration
@EnableConfigurationProperties(ContextualColorProperties.class)
public class ShellConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public Observer observer() {
    return new ProgressUpdateObserver();
  }

  @Bean
  public ShellHelper shellHelper(ContextualColorProperties properties, @Lazy Terminal terminal) {
    return new ShellHelper(properties, terminal);
  }

  @Bean
  public InputReader inputReader(
      @Lazy Terminal terminal,
      @Lazy Parser parser,
      JLineShellAutoConfiguration.CompleterAdapter completer,
      @Lazy History history,
      ShellHelper shellHelper) {
    LineReaderBuilder lineReaderBuilder =
        LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(completer)
            .history(history)
            .highlighter(
                (LineReader reader, String buffer) ->
                    new AttributedString(
                        buffer,
                        AttributedStyle.BOLD.foreground(
                            PromptColor.WHITE.toJlineAttributedStyle())))
            .parser(parser);

    LineReader lineReader = lineReaderBuilder.build();
    lineReader.unsetOpt(LineReader.Option.INSERT_TAB);
    return new InputReader(lineReader, shellHelper);
  }

  @Bean
  public ProgressCounter progressCounter(@Lazy Terminal terminal) {
    return new ProgressCounter(terminal);
  }

  @Bean
  public ProgressBar progressBar(ShellHelper shellHelper) {
    return new ProgressBar(shellHelper);
  }
}
