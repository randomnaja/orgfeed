package thaisamut.nbs.struts.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import randomnaja.orgfeed.interceptor.StrutsConfigAware;
import thaisamut.commons.struts2.SpringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:chalermpong.ch@ocean.co.th">Chalermpong Chaiyawan</a>
 */
public abstract class BaseAction extends ActionSupport
    implements ServletRequestAware, ServletResponseAware, ApplicationAware,
        ParameterAware, RequestAware, SessionAware, ServletContextAware, StrutsConfigAware
{
    protected HttpServletRequest request;

    protected String defaultEncoding;

    protected String uuid;

    protected HttpServletResponse response;

    protected Map sessionAttributes;

    protected Map appAttributes;

    protected Map requestParameters;

    protected Map requestAttributes;

    protected String actionExtension;

    protected ServletContext servletContext;

    protected ActionConfig strutsConfig;

    protected String strutsPackageNameSpace;

    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String s)
    {
        this.defaultEncoding = s;
    }

    public String getDefaultEncoding()
    {
        return this.defaultEncoding;
    }

    @Inject(StrutsConstants.STRUTS_ACTION_EXTENSION)
    public void setActionExtension(String s)
    {
        this.actionExtension = s;
    }

    public String getActionExtension()
    {
        return this.actionExtension;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public void setRequest(final Map params)
    {
        this.requestAttributes = params;
    }

    public Map getRequest()
    {
        return requestAttributes;
    }

    public void setServletRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    public void setSession(final Map session)
    {
        this.sessionAttributes = session;
    }

    public void setServletContext(ServletContext context)
    {
        servletContext = context;
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }

    public String index()
        throws Exception
    {
        return SUCCESS;
    }

    public String ping()
        throws Exception
    {
        return NONE;
    }

    public void setApplication(final Map application)
    {
        this.appAttributes = application;
    }

    public void setParameters(final Map params)
    {
        this.requestParameters = params;
    }

    protected Object getBean(String name)
        throws BeansException
    {
        return SpringUtils.getBean(name);
    }

    protected <T> T getBean(Class<T> type)
        throws BeansException
    {
        return SpringUtils.getBean(type);
    }

    protected <T> T getBean(String name, Class<T> type)
        throws BeansException
    {
        return SpringUtils.getBean(name, type);
    }

    private ApplicationContext getSpringContext()
    {
        return SpringUtils.getContext();
    }

    @Override
    public void setStrutsConfig(ActionConfig config) {
        this.strutsConfig = config;
    }

    @Override
    public void setStrutsPackageNameSpace(String nameSpace) {
        this.strutsPackageNameSpace = nameSpace;
    }

    public String getStrutsPackageNameSpace() {
        return strutsPackageNameSpace;
    }

    public String getPackageURI() {
        return servletContext.getContextPath() + strutsPackageNameSpace;
    }

    public ActionConfig getStrutsConfig() {
        return strutsConfig;
    }

    public String getContextPath() {
        return servletContext.getContextPath();
    }
}
