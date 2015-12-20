package randomnaja.orgfeed.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class StrutsConfigInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        if (invocation.getAction() instanceof StrutsConfigAware) {
            ActionConfig actionConfig = invocation.getProxy().getConfig();
            String namespace = invocation.getProxy().getNamespace();
            ((StrutsConfigAware) invocation.getAction()).setStrutsConfig(actionConfig);
            ((StrutsConfigAware) invocation.getAction()).setStrutsPackageNameSpace(namespace);
        }

        return invocation.invoke();
    }
}
