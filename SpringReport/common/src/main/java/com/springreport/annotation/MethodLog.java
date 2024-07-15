package com.springreport.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 表示对标记有xxx注解的类,做代理 注解@Retention可以用来修饰注解，是注解的注解，称为元注解。 
 * Retention注解有一个属性value，是RetentionPolicy类型的，Enum RetentionPolicy是一个枚举类型， 
 * 这个枚举决定了Retention注解应该如何去保持，也可理解为Rentention 搭配 
 * RententionPolicy使用。RetentionPolicy有3个值：CLASS RUNTIME SOURCE 
 * 用@Retention(RetentionPolicy 
 * .CLASS)修饰的注解，表示注解的信息被保留在class文件(字节码文件)中当程序编译时，但不会被虚拟机读取在运行的时候； 
 * 用@Retention(RetentionPolicy.SOURCE 
 * )修饰的注解,表示注解的信息会被编译器抛弃，不会留在class文件中，注解的信息只会留在源文件中； 
 * 用@Retention(RetentionPolicy.RUNTIME 
 * )修饰的注解，表示注解的信息被保留在class文件(字节码文件)中当程序编译时，会被虚拟机保留在运行时， 
 * 所以他们可以用反射的方式读取。RetentionPolicy.RUNTIME 
 * 可以让你从JVM中读取Annotation注解的信息，以便在分析程序的时候使用. 
 *  
 * 类和方法的annotation缺省情况下是不出现在javadoc中的，为了加入这个性质我们用@Documented 
 *  java用  @interface Annotation{ } 定义一个注解 @Annotation，一个注解是一个类。 
 *  @interface是一个关键字，在设计annotations的时候必须把一个类型定义为@interface，而不能用class或interface关键字  
 */

/**
 * @Description 在方法上标记操作备注，及操作类型，用于记录用户操作日志描述
 * @author caiyang
 * @Date 2020年5月29日13:19:28
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {
	String module() default "";  //功能模块
	String remark() default ""; // 操作备注
	String operateType() default ""; // 操作类型：Add/Update/Delete/Search/Login等
}
