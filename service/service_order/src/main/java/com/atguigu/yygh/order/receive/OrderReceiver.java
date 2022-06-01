package com.atguigu.yygh.order.receive;

import com.atguigu.common.rabbit.constant.MqConst;
import com.atguigu.yygh.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * @author wang
 * @create 2022-05-31
 */
@Component
public class OrderReceiver {
    @Autowired
    private OrderService orderService;
    //接收定时消息，监听的逻辑就是监听value值对应
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_TASK_8, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_TASK),
            key = {MqConst.ROUTING_TASK_8}
    ))
    public void patientTips(Message message, Channel channel) throws IOException {
        //定时任务，定时短信提醒病人挂号时间到了
        orderService.patientTips();
    }
}
