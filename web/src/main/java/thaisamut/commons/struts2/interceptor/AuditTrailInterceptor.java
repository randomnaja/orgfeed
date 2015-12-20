package thaisamut.commons.struts2.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import thaisamut.pantheonjmx.MetricsTimerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuditTrailInterceptor
    extends AbstractInterceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditTrailInterceptor.class);

    @Autowired
    private MetricsTimerFactory metricsFactory;

    @Override
    public String intercept(ActionInvocation invocation)
        throws Exception
    {
        HttpServletRequest request = ServletActionContext.getRequest();

        ActionContext context = invocation.getInvocationContext();
        ActionProxy proxy = invocation.getProxy();
        Map<String, Object> sesmap = context.getSession();
        ActionMapping mapping = ServletActionContext.getActionMapping();
        String login_id = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        String pathNameSpace = proxy.getNamespace();
        String action = pathNameSpace + "/" + proxy.getActionName()
            + "." + mapping.getExtension();
        String result = "DONE";
        String remote_addr = request.getRemoteAddr();

        long start = System.currentTimeMillis();

        try
        {
            LOG.info(new StrBuilder("User '")
                    .append(login_id).append("' is attempting to perform action '")
                    .append(action).append("' ...")
                    .toString());

            String invoc_result = invocation.invoke();

            if ("forbidden".equalsIgnoreCase(invoc_result)
                || "access-denied".equalsIgnoreCase(invoc_result))
            {
                result = "FAILED";
            }

            return invoc_result;
        }
        catch (Exception e)
        {
            result = "FAILED";

            throw e;
        }
        finally
        {
            if (StringUtils.isNotBlank((String) request.getAttribute("endResult")))
            {
                result = (String) request.getAttribute("endResult");
            }

            long elInMs = System.currentTimeMillis() - start;

            LOG.info(new StrBuilder("[")
                    .append(result)
                    .append("] - ").append(remote_addr).append(" ~ User '")
                    .append(login_id).append("' performs action '")
                    .append(action).append("', taken time is ")
                    .append(DurationFormatUtils.formatDurationHMS(elInMs)).toString());

            metricsFactory.getTimer("request").update(elInMs, TimeUnit.MILLISECONDS);
            metricsFactory.getTimer("request." + pathNameSpace).update(elInMs, TimeUnit.MILLISECONDS);
            if (result.equals("DONE")) {
                metricsFactory.getTimer("requestSuccess").update(elInMs, TimeUnit.MILLISECONDS);
                metricsFactory.getTimer("requestSuccess." + pathNameSpace)
                        .update(elInMs, TimeUnit.MILLISECONDS);
            } else {
                metricsFactory.getTimer("requestFail").update(elInMs, TimeUnit.MILLISECONDS);
                metricsFactory.getTimer("requestFail." + pathNameSpace)
                        .update(elInMs, TimeUnit.MILLISECONDS);
            }
        }

    }
}
