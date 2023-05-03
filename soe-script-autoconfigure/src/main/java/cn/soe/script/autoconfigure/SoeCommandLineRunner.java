package cn.soe.script.autoconfigure;

import cn.soe.boot.core.exception.BizException;
import cn.soe.boot.core.util.SoeUtils;
import cn.soe.utl.ClassUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.util.List;

/**
 * @author xiezhenxiang 2023/4/28
 */
public abstract class SoeCommandLineRunner implements CommandLineRunner {

    public static void main(String[] args) {
        List<Class<?>> subClass = ClassUtils.getAllSubClass(SoeCommandLineRunner.class);
        if (subClass.size() != 1) {
            throw new BizException("not find class or find size over 1 for class SoeCommandLineRunner");
        }
        Class<?> scriptMainClass = subClass.get(0);
        SpringApplication.run(scriptMainClass, args);
        SoeUtils.getBean(SoeCommandLineRunner.class).exec(args);
    }

    @Override
    public void run(String... args) {
    }

    protected abstract void exec(String... args);
}