package com.atguigu.yygh.order.aspect;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 用AOP的方式增强目标方法，增加分布式锁
 * Redisson - 是一个高级的分布式协调Redis客服端；redisson实现了分布式和可扩展的java数据结构，线程安全：封装了ReentrantLock
 * 获取不到锁则会一直阻塞自旋
 * @author wang
 * @create 2022-07-01
 */
@Aspect
@Component
public class OrderAspect {

    @Autowired
    private RedissonClient redissonClient;

    //为了防止用户快速点击导致重复预约检测不出来，同时该服务可能布置多个实例，还有超卖问题，所以采用分布式锁
    @Around("execution(public Long com.atguigu.yygh.order.service.impl.OrderServiceImpl.saveOrder(..))")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable{
        //JoinPoint对象封装了SpringAOP中切面方法的信息，在切面方法添加JoinPoint参数，就可以获取封装了该方法信息的JoinPoint对象

        //public Long saveOrder(String scheduleId, Long patientId)
        Object[] args = joinPoint.getArgs();
        String key = (String) args[0] + (Long) args[1];

        if (args.length == 2 && !StringUtils.isEmpty(args[0]) && !StringUtils.isEmpty(args[1])) {
            //定义锁，value要是唯一的，是UUID + 线程ID
            RLock lock = redissonClient.getLock(key);
            try {
                //上锁，10秒是设置的锁的有效期；它会尝试获取锁，如果没获取则会阻塞自旋；底层是用LUA脚本保证释放锁的原子性。
                lock.lock(10L, TimeUnit.SECONDS);
                Thread.sleep(5000);
                //执行目标方法
                return joinPoint.proceed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                //释放锁
                lock.unlock();
            }
        } else {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        return null;
    }
}
