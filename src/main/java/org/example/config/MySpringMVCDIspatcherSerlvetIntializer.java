package org.example.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.HiddenHttpMethodFilter;
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

    // импортируем метод для фильтра
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        registerHiddenFieldFilter(servletContext);
    }
    // тут мы создаем фильтр в спринге для обработки скрытого поля в html форме
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, null, null);
    }

}
