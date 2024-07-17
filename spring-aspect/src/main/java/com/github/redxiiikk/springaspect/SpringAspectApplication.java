package com.github.redxiiikk.springaspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringAspectApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAspectApplication.class, args);
  }

}


@RestController
class HelloController {
  @LogExecutionTime
  @GetMapping("/hello")
  public String hello() {
    System.out.println("P");
    return "Hello World";
  }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface LogExecutionTime {

}

@Aspect
@Component
class FAspect {
  @Around("@annotation(LogExecutionTime)")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    System.out.println("F B");

    Object result = pjp.proceed();

    System.out.println("F A");

    return result;
  }
}


@Aspect
@Component
class TAspect {
  @Around("@annotation(LogExecutionTime)")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    System.out.println("T B");

    Object result = pjp.proceed();

    System.out.println("T A");

    return result;
  }
}
