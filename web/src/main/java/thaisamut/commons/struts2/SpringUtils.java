package thaisamut.commons.struts2;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.apache.struts2.ServletActionContext;

/**
 * @author <a href="mailto:chalermpong.ch@ocean.co.th">Chalermpong Chaiyawan</a>
 */
public class SpringUtils implements ApplicationContextAware
{
    private static ApplicationContext context;

    private SpringUtils() { }

    public void setApplicationContext(ApplicationContext context)
        throws BeansException
    {
        this.context = context;
    }

    public static ApplicationContext getContext()
        throws BeansException
    {
        if (null == context)
        {
            return WebApplicationContextUtils.getWebApplicationContext(
                    ServletActionContext.getServletContext());
        }

        return context;
    }

    public static <T> T getBean(Class<T> type)
        throws BeansException
    {
        return getContext().getBean(type);
    }

    public static <T> T getBean(String name, Class<T> type)
        throws BeansException
    {
        return getContext().getBean(name, type);
    }

    public static Object getBean(String name)
        throws BeansException
    {
        return getContext().getBean(name);
    }
}
