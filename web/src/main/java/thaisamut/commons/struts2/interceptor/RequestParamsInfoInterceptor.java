package thaisamut.commons.struts2.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thaisamut.commons.security.UserSession;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;

public class RequestParamsInfoInterceptor
        extends AbstractInterceptor
{
    //~ Static fields/initializers ·············································

    private static final Logger LOG = LoggerFactory.getLogger(RequestParamsInfoInterceptor.class);

    //~ Constructors ···························································

    public RequestParamsInfoInterceptor()
    {
    }

    //~ Methods ································································

    public void init()
    {
    }

    public void destroy()
    {
    }

    public String intercept(final ActionInvocation invocation)
            throws Exception
    {
        if (LOG.isDebugEnabled())
        {
            HttpServletRequest request = ServletActionContext.getRequest();
            ActionMapping mapping = ServletActionContext.getActionMapping();
            ActionContext context = ServletActionContext.getActionContext(request);
            Map<String, Object> session = context.getSession();
            Map<String, Object> params = context.getParameters();
            ValueStack stack = context.getValueStack();
            ActionProxy proxy = invocation.getProxy();
            String namespace = proxy.getNamespace();
            String action = proxy.getActionName();
            String extension = mapping.getExtension();
            String login_id = "GUEST";
            Principal user = request.getUserPrincipal();
            StrBuilder buffer = new StrBuilder(LINE_SEPARATOR);

            if (null != user)
            {
                login_id = user.getName();
            }

            buffer.append("[ R E Q U E S T · P A R A M E T E R S ]··················································")
                  .append(LINE_SEPARATOR)
                  .append("User: ").append(login_id).append(LINE_SEPARATOR)
                  .append("Action: ").append(namespace).append("/")
                  .append(action).append(".").append(extension);

            if (params.size() == 0)
            {
                buffer.append(LINE_SEPARATOR).append("+-o [ N O N E ]");
            }
            else
            {
                for (String key : params.keySet())
                {
                    Object value = params.get(key);

                    if (value instanceof Object[])
                    {
                        Object[] values = (Object[]) value;

                        if (1 == values.length)
                        {
                            buffer.append(LINE_SEPARATOR)
                                  .append("+--o ").append(key).append(": ")
                                  .append(values[0]);
                        }
                        else
                        {
                            buffer.append(LINE_SEPARATOR).append("+-o ").append(key)
                                  .append(": ").append(StringUtils.join((Object[]) value, ", "));
                        }
                    }
                    else
                    {
                        buffer.append(LINE_SEPARATOR).append("+-o ")
                              .append(key).append(": ").append(value);
                    }
                }
            }

            if (null != user)
            {
                UserSession us = (UserSession) session.get(UserSession.KEY);
                Set<Integer> perms = null;

                if (null != us)
                {
                    Map<String, Object> credentials = us.getCredentials();

                    perms = (Set<Integer>) credentials.get("Permissions");
                }

                buffer.append(LINE_SEPARATOR)
                      .append("[ U S E R · P E R M I S S I O N S ]······················································")
                      .append(LINE_SEPARATOR);

                if ((null == perms) || (perms.size() == 0))
                {
                    buffer.append("[ N O N E ]");
                }
                else
                {
                    buffer.append(StringUtils.join(perms, ", "));
                }
            }

            buffer.append(LINE_SEPARATOR)
                  .append("~························································································");

            LOG.debug(buffer.toString());
        }

        return invocation.invoke();
    }
}


// vim:nu:ts=4:sts=4:sw=4:ft=java:et:ai:sm:ai:sta
