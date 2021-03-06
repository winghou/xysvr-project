/*
 * Copyright (c) 2014 Nanjing Xiaoyou Information Technology Co.,Ltd. All rights reserved.
 */

package cn.xyspace.xysvr.common.tool.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 性能监控拦截器。
 * 
 * @author ChenFangjie(2015/1/8 11:15:55)
 * 
 * @since 1.0.0
 * 
 * @version 1.0.0
 *
 */
public class PerformanceInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceInterceptor.class);

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long beginTime = System.currentTimeMillis(); // 1、开始时间
        this.startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）

        logger.warn("开始处理请求时间：{}", beginTime);

        return true; // 继续流程
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long endTime = System.currentTimeMillis(); // 2、结束时间
        long beginTime = this.startTimeThreadLocal.get(); // 得到线程绑定的局部变量（开始时间）
        long consumeTime = endTime - beginTime; // 3、消耗的时间

        // 记录到日志文件
        logger.warn("请求处理完成：请求  {} 耗时 {} 毫秒。", request.getRequestURI(), consumeTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis(); // 2、结束时间
        long beginTime = this.startTimeThreadLocal.get(); // 得到线程绑定的局部变量（开始时间）
        long consumeTime = endTime - beginTime; // 3、消耗的时间

        // if (consumeTime > 500) { // 此处认为处理时间超过500毫秒的请求为慢请求

        // 记录到日志文件
        logger.warn("渲染完成：请求  {} 耗时 {} 毫秒。", request.getRequestURI(), consumeTime);

        // }
    }

}
