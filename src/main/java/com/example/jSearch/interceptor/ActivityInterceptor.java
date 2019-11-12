package com.example.jSearch.interceptor;


import com.example.jSearch.entity.Activity;
import com.example.jSearch.service.ActivityService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ActivityInterceptor extends HandlerInterceptorAdapter {

    private final ActivityService activityService;

    @Autowired
    public ActivityInterceptor(ActivityService service) {
        activityService = service;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Activity activity = this.createActivity(request, response);
        if (!activity.getUrl().contains("image")
                && !activity.getUrl().equals("/")
                && !activity.getUrl().matches("[\\w/\\-_.]+\\.(css|woff2|js|xml|png|jpeg)")
                && activityService.findByParameter(activity.getParameters()) == null) {
            activity = activityService.save(activity);
            return super.preHandle(request, response, handler);
        } else if (activity.getUrl().equals("/")) {
            Activity existingActivity = this.activityService.findFirst();
            if (existingActivity != null) {
                activity.setId(existingActivity.getId());
                activity.setCreated(existingActivity.getCreated());
            } else {
                activity = this.activityService.save(activity);
            }
        }
        return super.preHandle(request, response, handler);
    }
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        String[] links = (String[]) modelAndView.getModel().get("queryResult");
        if (links != null) {
            Activity existedActivity = activityService.findByParameter(request.getParameter("q"));
            if (existedActivity != null) {
                existedActivity.setLinks(new Gson().toJson(links));
                existedActivity = activityService.save(existedActivity);
            } else {
                Activity newActivity = this.createActivity(request, response);
                newActivity.setLinks(new Gson().toJson(links));
                newActivity = activityService.save(newActivity);
            }
            super.postHandle(request, response, handler, modelAndView);
        }
        super.postHandle(request, response, handler, modelAndView);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return new StringTokenizer(xForwardForHeader, ",").nextToken().trim();
        }
    }

    private Activity createActivity(HttpServletRequest request, HttpServletResponse response){
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) userAgent = request.getHeader("user-agent");
        String expires = response.getHeader("expires");
        Activity activity = new Activity();
        activity.setIp(this.getClientIpAddress(request));
        activity.setExpires(expires);
        activity.setRequestMethod(request.getMethod());
        activity.setUrl(request.getRequestURI());
        activity.setParameters(request.getParameter("q"));
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);
        if (m.find()) {
            activity.setUserAgent(m.group(1));
        }
        return activity;
    }
}
