package view.nishat.net.BackingBean;

import java.io.OutputStream;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import view.nishat.net.Helper.AuthHandler;
import view.nishat.net.Helper.CommonUtil;

public class ConfirmPasswordPage {
    private RichInputText currentPassword;
    private RichInputText newPassword;
    private RichInputText reEnterPassword;

    public ConfirmPasswordPage() {
      
    }

    public String changePassword() {
        String username = CommonUtil.getSessionValue("username").toString();
        String temp_no = CommonUtil.getSessionValue("temp_pass").toString();
        
        String _currentPassword = currentPassword.getValue().toString();
        if (!temp_no.equals(_currentPassword)) {
            CommonUtil.showMessage("You must enter your current password correctly in order to change your password. Password change cancelled.", 112);
        }else{
            if (!newPassword.getValue().toString().equals(reEnterPassword.getValue().toString())) {
                CommonUtil.showMessage("Password Not Matching", 112);    
            }else{
                AuthHandler ah = new AuthHandler();
                try {
                    int updateID = ah.updatePassword(newPassword.getValue().toString(), username, temp_no);
                    if (updateID ==1) {
                        CommonUtil.destroySession();
                        CommonUtil.redirect("login_page.jspx");
                    }
                } catch (ClassNotFoundException e) {
                    CommonUtil.showMessage("Take Screenshot and email it to I.C.T - "+e.getMessage()+" ConfirmPasswordPage.java line 34", 112);
                }
            }
            
            
        }
        return null;
    }

    public void setCurrentPassword(RichInputText currentPassword) {
        this.currentPassword = currentPassword;
    }

    public RichInputText getCurrentPassword() {
        return currentPassword;
    }

    public void setNewPassword(RichInputText newPassword) {
        this.newPassword = newPassword;
    }

    public RichInputText getNewPassword() {
        return newPassword;
    }

    public void setReEnterPassword(RichInputText reEnterPassword) {
        this.reEnterPassword = reEnterPassword;
    }

    public RichInputText getReEnterPassword() {
        return reEnterPassword;
    }

    public String cancelUpdatePassword() {
        CommonUtil.redirect("login_page.jspx");
        return null;
    }
}
