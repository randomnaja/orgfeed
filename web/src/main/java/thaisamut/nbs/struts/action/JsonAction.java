package thaisamut.nbs.struts.action;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:chalermpong.ch@ocean.co.th">Chalermpong Chaiyawan</a>
 */
public class JsonAction extends BaseAction
{
    public static class Result<T> implements Serializable
    {
        protected T data;
        protected String errorMessage;
        protected String redirectURL;
        protected String message;

        public T getData() { return data; }
        public void setData(T o) { data = o; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String s) { errorMessage = s; }
        public String getRedirectURL() { return redirectURL; }
        public void setRedirectURL(String s) { redirectURL = s; }
        public String getMessage() { return message; }
        public void setMessage(String s) { message = s; }
    }

    protected abstract class Processor
    {
        public String run()
        {
            try
            {
                perform();
            }
            catch (Exception e)
            {
                result.setErrorMessage(e.getMessage() == null ? "NPE" : e.getMessage());

                LOG.error(e.getMessage(), e);
            }
            finally
            {
                if (isNotBlank(result.getErrorMessage()))
                {
                    getRequest().put("endResult", "FAILED");
                }
            }

            return SUCCESS;
        }

        protected abstract void perform() throws Exception;
    }

    protected Result result;

    public JsonAction()
    {
        result = new Result();
    }

    public <T extends Result> T getResult() { return (T) result; }
}
