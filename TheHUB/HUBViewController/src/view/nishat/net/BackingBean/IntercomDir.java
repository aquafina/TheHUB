package view.nishat.net.BackingBean;

import hub.nishat.net.model.AM.HubModuleImpl;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jbo.VariableValueManager;
import oracle.jbo.server.ViewObjectImpl;

import view.nishat.net.Helper.CommonUtil;

public class IntercomDir {
    private RichSelectOneChoice intercom_type;
    private String intercomType;

    public IntercomDir() {
    }

    public String applySearch() {
        // Add event code here...
        
        search();
        return null;
    }
    
    public void search(){
        HubModuleImpl am = (HubModuleImpl)CommonUtil.getAppModule();
        ViewObjectImpl vo = am.getVO_IntercomDir1();
        oracle.jbo.ViewCriteria vc = vo.getViewCriteria("VO_IntercomDirCriteria");
        vc.resetCriteria();
        VariableValueManager vvm = vc.ensureVariableManager();
        vvm.clearValue("bv_intercom_type");
        if (CommonUtil.getValueFrmExpression("#{bindings.Type.attributeValue}") == null) {
            intercomType = "";
        }else{
            intercomType = CommonUtil.getValueFrmExpression("#{bindings.Type.attributeValue}").toString();
        }
        System.out.println("Search: "+intercomType);
        vvm.setVariableValue("bv_intercom_type",intercomType);
        vo.applyViewCriteria(vc, true);
        vo.executeQuery();
    }

    public void setIntercom_type(RichSelectOneChoice intercom_type) {
        this.intercom_type = intercom_type;
    }

    public RichSelectOneChoice getIntercom_type() {
        return intercom_type;
    }

    public void intercomTypeChange(ValueChangeEvent valueChangeEvent) {
        CommonUtil.setvalueToExpression("#{bindings.Type.inputValue}",
                                        valueChangeEvent.getNewValue()); //Updating Model Values
        search();
        
                                            
        
    }
}
