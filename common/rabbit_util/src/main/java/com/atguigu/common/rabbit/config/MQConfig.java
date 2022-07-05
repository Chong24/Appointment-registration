package com.atguigu.common.rabbit.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AMPQ协议：高级消息队列协议，使得遵从该规范的客户端应用和消息中间件服务器的全功能互操作成为可能
 * @author wang
 * @create 2022-05-31
 */
@Configuration
public class MQConfig {
    //对消息转化
    //Message 是 Spring AMQP 对消息的封装。两个重要的属性：body：消息内容。 messageProperties：消息属性。
    // Spring-AMQP 允许我们直接使用自定义的类，然后会利用指定好的 MessageConverter 将自定义的类转换为 Message 进行发送，
    // 在接收时也会利用 MessageConverter 将接收到的 Message 对象转成需要的对象。
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}