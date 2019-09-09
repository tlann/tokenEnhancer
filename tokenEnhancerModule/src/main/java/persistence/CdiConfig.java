package persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sun.tools.java.Environment;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence-postgres.properties" })
@ComponentScan({ "mil.noms.domain.user.models.v1"})
@EnableJpaRepositories(basePackages = "mil.noms.repository.user.repositories.v1")
public class CdiConfig {


    @Autowired
    private Environment env;


}
