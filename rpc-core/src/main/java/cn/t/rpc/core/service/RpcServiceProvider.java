package cn.t.rpc.core.service;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcServiceProvider {
    Class<?> interfaceClass() default void.class;
}
