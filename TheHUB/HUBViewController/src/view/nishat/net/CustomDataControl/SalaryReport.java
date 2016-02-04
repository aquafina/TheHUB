package view.nishat.net.CustomDataControl;

import com.oracle.xmlns.pcbpel.adapter.db.top.readempsalary.HrPayHistory;

import com.oracle.xmlns.pcbpel.adapter.db.top.readempsalary.HrPayHistoryCollection;

import getemppayrollrec.PayHistory;

import java.util.ArrayList;
import java.util.List;

import view.nishat.net.Helper.CommonUtil;
import view.nishat.net.Helper.Constants;

public class SalaryReport {
    private List<EmpPayHistory> salaryReport;
    public SalaryReport(){
        salaryReport = new ArrayList<EmpPayHistory>();
        PayHistory p = new PayHistory();


        try {
            List<HrPayHistory> list= p.getPayHistory(CommonUtil.getSessionValue(Constants.SESSION_EMP_CODE).toString(), "201504");
            
            
        } catch (Exception e) {
        }
        EmpPayHistory payHistory1 = new EmpPayHistory();
        payHistory1.setEmpSalary("1111");
        payHistory1.setMonth("May");
        EmpPayHistory payHistory2 = new EmpPayHistory();
        payHistory2.setEmpSalary("1111");
        payHistory2.setMonth("June");
        EmpPayHistory payHistory3 = new EmpPayHistory();
        payHistory3.setEmpSalary("1111");
        payHistory3.setMonth("July");
        EmpPayHistory payHistory4 = new EmpPayHistory();
        payHistory4.setEmpSalary("1111");
        payHistory4.setMonth("August");
        EmpPayHistory payHistory5 = new EmpPayHistory();
        payHistory5.setEmpSalary("1111");
        payHistory5.setMonth("September");
        EmpPayHistory payHistory6 = new EmpPayHistory();
        payHistory6.setEmpSalary("1111");
        payHistory6.setMonth("October");
        EmpPayHistory payHistory7 = new EmpPayHistory();
        payHistory7.setEmpSalary("1111");
        payHistory7.setMonth("November");
        salaryReport.add(payHistory1);
        salaryReport.add(payHistory2);
        salaryReport.add(payHistory3);
        salaryReport.add(payHistory4);
        salaryReport.add(payHistory5);
        salaryReport.add(payHistory6);
        salaryReport.add(payHistory7);
    }

    public void setSalaryReport(List<EmpPayHistory> salaryReport) {
        this.salaryReport = salaryReport;
    }

    public List<EmpPayHistory> getSalaryReport() {
        return salaryReport;
    }
}
