package ccc.cj.siber.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ccc.cj.siber", "springfox"})
public class RootConfig {
}