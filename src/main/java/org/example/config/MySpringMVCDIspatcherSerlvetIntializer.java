package org.example.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMVCDIspatcherSerlvetIntializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    //
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class}; // вот тут как раз  возвращает конфигурационные классы для контекста DispatcherServlet
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"}; // какие URL будут обрабатываться т.е через слеш
    }

}
