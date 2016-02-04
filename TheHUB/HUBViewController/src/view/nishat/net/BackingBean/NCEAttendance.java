package view.nishat.net.BackingBean;

import javax.faces.event.ValueChangeEvent;

import view.nishat.net.Helper.CommonUtil;

public class NCEAttendance {
    public NCEAttendance() {
    }

    public void NceEmpLovChanged(ValueChangeEvent valueChangeEvent) {
        CommonUtil.setvalueToExpression("#{bindings.FullName.inputValue}",
                                        valueChangeEvent.getNewValue());
        String id = CommonUtil.getValueFrmExpression("#{bindings.PersonId.inputValue}");
        CommonUtil.log("id:"+id);
    }
}
