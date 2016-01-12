package view.nishat.net.BackingBean;

import hub.nishat.net.model.AM.HubModuleImpl;
import hub.nishat.net.model.VO.CEO.AM.CEOAppModuleImpl;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.CommonUtil;

public class EmployeeInfo4Supervisor {
    private RichCommandButton toggleAttendanceButton;

    public EmployeeInfo4Supervisor() {
    }

    public void beforeRenderingPhase(PhaseEvent phaseEvent) {
        // Add event code here...
        if (phaseEvent.getPhaseId().toString().equals("RENDER_RESPONSE 6")) {

            String emp_id =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("emp_id");
            if (emp_id != null) {
                HubModuleImpl am =
                    (HubModuleImpl)CommonUtil.getAppModule();

                ViewObjectImpl vo = am.getVO_EmpAttendance1();

                String where =
                    "EMP_ID = '" + emp_id + "' and (to_char(attendance_date,'YYYYMON') = to_char(sysdate,'YYYYMON') or to_char(attendance_date,'YYYYMON') = to_char(add_months(sysdate,-1),'YYYYMON') )";
                CommonUtil.resetWhereClause(vo);
                vo.setWhereClause(where);
                vo.executeQuery();
            }

        }
    }

    public String toggleAttendance() {
        // Add event code here...
        String btnText = toggleAttendanceButton.getText();
        if (btnText.equals("Current")) {
            //showAttendance("Current");
            toggleAttendanceButton.setText("Previous");
        }else if (btnText.equals("Previous")){
            //showAttendance("Previous");
            toggleAttendanceButton.setText("Current");
        }
        return null;
    }

    public void setToggleAttendanceButton(RichCommandButton toggleAttendanceButton) {
        this.toggleAttendanceButton = toggleAttendanceButton;
    }

    public RichCommandButton getToggleAttendanceButton() {
        return toggleAttendanceButton;
    }
}
