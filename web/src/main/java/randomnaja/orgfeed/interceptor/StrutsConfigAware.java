package randomnaja.orgfeed.interceptor;

import com.opensymphony.xwork2.config.entities.ActionConfig;

public interface StrutsConfigAware {

    void setStrutsConfig(ActionConfig config);

    void setStrutsPackageNameSpace(String nameSpace);
}
