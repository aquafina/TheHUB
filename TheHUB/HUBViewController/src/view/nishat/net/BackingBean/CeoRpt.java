package view.nishat.net.BackingBean;

import hub.nishat.net.model.AM.HubModuleImpl;

import hub.nishat.net.model.VO.CEO.AM.CEOAppModuleImpl;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.CommonUtil;

public class CeoRpt {
    public CeoRpt() {
    }

    public void beforeLoading(PhaseEvent phaseEvent) {
            if (phaseEvent.getPhaseId().toString().equals("RENDER_RESPONSE 6")) {

                String emp_id =
                    FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("emp_id");
                if (emp_id != null) {
                    
                    CEOAppModuleImpl am = (CEOAppModuleImpl)CommonUtil.getCEOModule();
                    
                    
                    ViewObjectImpl vo =  am.getVO_EmpAttendance1();
                    
                    String where = "EMP_ID = '"+emp_id+"' and to_char(attendance_date,'YYYYMON') = to_char(sysdate,'YYYYMON')";
                    CommonUtil.resetWhereClause(vo);
                    vo.setWhereClause(where);
                    vo.executeQuery();
                    
                    
                }

            }
    }
}
