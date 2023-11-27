package ru.javawebinar.topjava.web;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

@Component
public class MyViewResolver implements ViewResolver {

    private ViewResolver cssResolver;
    private ViewResolver jspResolver;

    public void setJspResolver(ViewResolver jspResolver) {
        this.jspResolver = jspResolver;
    }

    public void setCssResolver(ViewResolver cssResolver) {
        this.cssResolver = cssResolver;
    }

    @Override
    public View resolveViewName(@NonNull String viewName, @NonNull Locale locale) throws Exception {
        if ("style".equals(viewName)){
            return cssResolver.resolveViewName(viewName, locale);
        }
        else {
            return jspResolver.resolveViewName(viewName, locale);
        }
    }
}
