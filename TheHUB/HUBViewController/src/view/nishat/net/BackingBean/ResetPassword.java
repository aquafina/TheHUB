package view.nishat.net.BackingBean;

import javax.faces.event.PhaseEvent;

import oracle.adf.controller.v2.lifecycle.PageLifecycle;
import oracle.adf.controller.v2.lifecycle.PagePhaseListener;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import view.nishat.net.Helper.AuthHandler;
import view.nishat.net.Helper.CommonUtil;

public class ResetPassword {

    private String message;
    private RichOutputText messageView;

    public void resetPassword(PhaseEvent phaseEvent) {

        if (phaseEvent.getPhaseId().toString().equals("RENDER_RESPONSE 6")) {
            String email = CommonUtil.getSessionValue("email").toString();
            //RESET PASSWORD AGAINST THE ABOVE EMAIL ADDRESS

            AuthHandler ah = new AuthHandler();

            System.out.println(phaseEvent.getPhaseId().toString() +
                               "In the listener");
            try {
                int result = ah.resetPassword(email);
                if (result == 1) {
                    CommonUtil.showMessage("success", 111);
                } else {
                    CommonUtil.showMessage("Failed", 112);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageView(RichOutputText messageView) {
        this.messageView = messageView;
    }

    public RichOutputText getMessageView() {
        return messageView;
    }


}
