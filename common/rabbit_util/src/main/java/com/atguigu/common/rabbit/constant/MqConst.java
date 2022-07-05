package com.atguigu.common.rabbit.constant;

/**
 * @author wang
 * @create 2022-05-31
 */
public class MqConst {
    /**
     * 预约下单
     */
    public static final String EXCHANGE_DIRECT_ORDER = "exchange.direct.order";
    public static final String ROUTING_ORDER = "order";
    //队列
    public static final String QUEUE_ORDER = "queue.order";

    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_MSM = "exchange.direct.msm";
    public static final String ROUTING_MSM_ITEM = "msm.item";
    //队列
    public static final String QUEUE_MSM_ITEM = "queue.msm.item";

    /**
     * 用户
     */
    public static final String EXCHANGE_DIRECT_User = "exchange.direct.user";
    public static final String ROUTING_USER_ITEM = "user.item";
    //队列
    public static final String QUEUE_User_ITEM = "queue.user.item";

    /**
     * 定时任务：就医提醒
     */
    public static final String EXCHANGE_DIRECT_TASK = "exchange.direct.task";
    public static final String ROUTING_TASK_8 = "task.8";
    //队列
    public static final String QUEUE_TASK_8 = "queue.task.8";
}