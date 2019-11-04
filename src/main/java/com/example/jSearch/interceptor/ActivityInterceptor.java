package com.example.jSearch.interceptor;


import com.example.jSearch.entity.Activity;
import com.example.jSearch.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ActivityInterceptor extends HandlerInterceptorAdapter {

    private final ActivityService activityService;

    @Autowired
    public ActivityInterceptor(ActivityService service){
        activityService = service;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String userAgent = request.getHeader("User-Agent");
        if(userAgent == null) userAgent = request.getHeader("user-agent");
        String expires = response.getHeader("Expires");
        Activity activity = new Activity();
        activity.setIp(this.getClientIpAddress(request));
        activity.setExpires(expires);
        activity.setRequestMethod(request.getMethod());
        activity.setUrl(request.getRequestURI());

        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);

        if (m.find()){
            activity.setUserAgent(m.group(1));
        }
        if(!activity.getUrl().contains("image") && !activity.getUrl().equals("/")){
            activity = activityService.save(activity);
            return super.preHandle(request, response, handler);
        }else if(activity.getUrl().equals("/")){
            Activity existingActivity = this.activityService.findFirst();
            if(existingActivity != null){
                activity.setId(existingActivity.getId());
                activity.setCreated(existingActivity.getCreated());
                long totalVisitors = existingActivity.getTotalVisitors();
                activity.setTotalVisitors(++totalVisitors);
            }else {
                activity.setTotalVisitors(1L);
                activity = this.activityService.save(activity);
            }
        }
        return super.preHandle(request, response, handler);
    }


    private String getClientIpAddress(HttpServletRequest request){
        String xForwardForHeader = request.getHeader("X-Forwarded-For");
        if(xForwardForHeader == null){
            return request.getRemoteAddr();
        }else {
            return new StringTokenizer(xForwardForHeader, ",").nextToken().trim();
        }

    }
}
