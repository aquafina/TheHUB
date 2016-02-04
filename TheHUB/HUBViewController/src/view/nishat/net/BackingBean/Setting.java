package view.nishat.net.BackingBean;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import sendemail.SendEmail;
import sendemail.SendMail;

import view.nishat.net.Helper.AuthHandler;
import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;

public class Setting {
    private RichInputText oldPassword;
    private RichInputText newPassword;
    private RichInputText reEnterNew;
    private RichInputText recoveryEmail;

    public Setting() {
    }

    public String changePassword() {
        String username =
            CommonUtil.getSessionValue(Constants.SESSION_USERNAME).toString();
        String password = oldPassword.getValue().toString();

        String _newPassword1 = newPassword.getValue().toString();
        String _newPassword2 = reEnterNew.getValue().toString();
        System.out.println();


        if (!_newPassword1.equals(_newPassword2)) {
            CommonUtil.showMessage("Password Mismatching.", 112);
        } else {
            AuthHandler ah = new AuthHandler();
            try {
                int id = ah.changePassword(username, _newPassword1, password);
                if (id == 1) {
                    CommonUtil.destroySession();
                    CommonUtil.redirect("login_page.jspx");
                }else if (id == 0) {
                    CommonUtil.showMessage("Your current password is wrong, try agian!", 112);
                }else if (id == -1) {
                    
                }
            } catch (ClassNotFoundException e) {
                
            }
        }


        return null;
    }

    public void setOldPassword(RichInputText oldPassword) {
        this.oldPassword = oldPassword;
    }

    public RichInputText getOldPassword() {
        return oldPassword;
    }

    public void setNewPassword(RichInputText newPassword) {
        this.newPassword = newPassword;
    }

    public RichInputText getNewPassword() {
        return newPassword;
    }

    public void setReEnterNew(RichInputText reEnterNew) {
        this.reEnterNew = reEnterNew;
    }

    public RichInputText getReEnterNew() {
        return reEnterNew;
    }

    public String resetPassword() {
        SendMail sm = new SendMail();
        sm.callWS(recoveryEmail.getValue().toString(), "");
        CommonUtil.showMessage("A link has been sent to your email. follow the link to complete process", 111);
        return null;
    }

    public void setRecoveryEmail(RichInputText recoveryEmail) {
        this.recoveryEmail = recoveryEmail;
    }

    public RichInputText getRecoveryEmail() {
        return recoveryEmail;
    }
}
