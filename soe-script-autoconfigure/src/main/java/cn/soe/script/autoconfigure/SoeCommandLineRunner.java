package cn.soe.script.autoconfigure;

import cn.soe.boot.core.util.SoeUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

/**
 * @author xiezhenxiang 2023/4/28
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
public abstract class SoeCommandLineRunner implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SoeCommandLineRunner.class, args);
        String[] beanNames = SoeUtils.getApplicationContext().getBeanNamesForType(SoeCommandLineRunner.class);
        SoeCommandLineRunner runner = SoeUtils.getBean(beanNames[1], SoeCommandLineRunner.class);
        runner.exec(args);
    }

    @Override
    public void run(String... args) {
    }

    protected abstract void exec(String... args);
}