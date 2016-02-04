package hub.nishat.net.model.VO.Supervisor;

import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Jul 03 13:30:04 PKT 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class VO_LeaveRequestsImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public VO_LeaveRequestsImpl() {
    }

    /**
     * Returns the bind variable value for parent_id.
     * @return bind variable value for parent_id
     */
    public String getparent_id() {
        //return (String)getNamedWhereClauseParam("parent_id");
        return CommonUtil.getSessionValue(Constants.SESSION_USERID).toString();
        //return "818";
    }

    /**
     * Sets <code>value</code> for bind variable parent_id.
     * @param value value to bind as parent_id
     */
    public void setparent_id(String value) {
        setNamedWhereClauseParam("parent_id", value);
    }
}