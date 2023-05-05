package cn.soe.boot.autoconfigure.web.validator.group;

import javax.validation.groups.Default;

/**
 * 如果继承了Default，@Validated标注的注解也会校验未指定分组或者Default分组的参数，比如@Email
 * 如果不继承Default则不会校验未指定分组的参数，需要加上@Validated(value = {ValidGroup.Crud.Update.class, Default.class}才会校验
 * #@GroupSequence注解可以指定校验顺序
 * @author xiezhenxiang 2023/5/5
 */
public interface VdGroup extends Default {

    interface Create extends VdGroup{

    }

    interface Update extends VdGroup{

    }

    interface Query extends VdGroup{

    }

    interface Delete extends VdGroup{

    }
}

/*
@Valid 和 @Validated
(1) @Valid 和 @Validated 两者都可以对数据进行校验，待校验字段上打的规则注解（@NotNull, @NotEmpty等）都可以对 @Valid 和 @Validated 生效；
(2) @Valid 进行校验的时候，需要用 BindingResult 来做一个校验结果接收。当校验不通过的时候，如果手动不 return ，则并不会阻止程序的执行；
(3) @Validated 进行校验的时候，当校验不通过的时候，程序会抛出400异常，阻止方法中的代码执行，这时需要再写一个全局校验异常捕获处理类，然后返回校验提示。
(4) @Validated可以看作是@Valid的加强注解，@Valid能只能作用在方法、属性、构造、参数上，而@Validated可以作用在类上。
(5) @Validated注解用在控制类上，会将类中的所有方法都开启参数校验,只有作用在类上，GET方式的请求才会校验。作用在方法上，只有POST请求校验（针对集合的非空验证这种情况不生效），GET请求校验不会生效。
原文链接：
https://blog.csdn.net/sunnyzyq/article/details/103527380
https://blog.csdn.net/tian830937/article/details/115555903
 */