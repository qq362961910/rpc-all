package cn.t.rpc.core.service;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcServiceConsumer {
    Class<?> interfaceClass() default void.class;
}
