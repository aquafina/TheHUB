package hub.nishat.net.model.VO.HR;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.domain.Number;
import oracle.jbo.jbotester.load.SimpleDateFormatter;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.server.ViewRowImpl;

import view.nishat.net.Helper.CommonUtil;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Nov 27 11:02:36 PKT 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class VO_PARTTIMESRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        TotalMissed {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getTotalMissed();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setTotalMissed((String)value);
            }
        }
        ,
        JoiningDate {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getJoiningDate();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setJoiningDate((String)value);
            }
        }
        ,
        TotalWorked {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getTotalWorked();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setTotalWorked((String)value);
            }
        }
        ,
        Total {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getTotal();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setTotal((String)value);
            }
        }
        ,
        EmpId {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getEmpId();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setEmpId((Number)value);
            }
        }
        ,
        EmpType {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getEmpType();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setEmpType((Number)value);
            }
        }
        ,
        Day {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getDay();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setDay((String)value);
            }
        }
        ,
        Month {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getMonth();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setMonth((String)value);
            }
        }
        ,
        FinalMissed {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getFinalMissed();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setFinalMissed((String)value);
            }
        }
        ,
        CalculatedLeaves {
            public Object get(VO_PARTTIMESRowImpl obj) {
                return obj.getCalculatedLeaves();
            }

            public void put(VO_PARTTIMESRowImpl obj, Object value) {
                obj.setCalculatedLeaves((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(VO_PARTTIMESRowImpl object);

        public abstract void put(VO_PARTTIMESRowImpl object, Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static final int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    private double finalMissedMinutesGlobal = 0;
    private final int HOURS_PER_DAY = 8;


    public static final int TOTALMISSED = AttributesEnum.TotalMissed.index();
    public static final int JOININGDATE = AttributesEnum.JoiningDate.index();
    public static final int TOTALWORKED = AttributesEnum.TotalWorked.index();
    public static final int TOTAL = AttributesEnum.Total.index();
    public static final int EMPID = AttributesEnum.EmpId.index();
    public static final int EMPTYPE = AttributesEnum.EmpType.index();
    public static final int DAY = AttributesEnum.Day.index();
    public static final int MONTH = AttributesEnum.Month.index();
    public static final int FINALMISSED = AttributesEnum.FinalMissed.index();
    public static final int CALCULATEDLEAVES = AttributesEnum.CalculatedLeaves.index();

    /**
     * This is the default constructor (do not remove).
     */
    public VO_PARTTIMESRowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute JoiningDate.
     * @return the JoiningDate
     */
    public String getJoiningDate() {
        return (String) getAttributeInternal(JOININGDATE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute JoiningDate.
     * @param value value to set the  JoiningDate
     */
    public void setJoiningDate(String value) {
        setAttributeInternal(JOININGDATE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute TotalWorked.
     * @return the TotalWorked
     */
    public String getTotalWorked() {
        return (String) getAttributeInternal(TOTALWORKED);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute TotalWorked.
     * @param value value to set the  TotalWorked
     */
    public void setTotalWorked(String value) {
        setAttributeInternal(TOTALWORKED, value);
    }

    /**
     * Gets the attribute value for the calculated attribute TotalMissed.
     * @return the TotalMissed
     */
    public String getTotalMissed() {
        return (String) getAttributeInternal(TOTALMISSED);
        //return getFinalMissed();
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute TotalMissed.
     * @param value value to set the  TotalMissed
     */
    public void setTotalMissed(String value) {
        setAttributeInternal(TOTALMISSED, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Total.
     * @return the Total
     */
    public String getTotal() {
        return (String) getAttributeInternal(TOTAL);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Total.
     * @param value value to set the  Total
     */
    public void setTotal(String value) {
        setAttributeInternal(TOTAL, value);
    }

    /**
     * Gets the attribute value for the calculated attribute EmpId.
     * @return the EmpId
     */
    public Number getEmpId() {
        return (Number) getAttributeInternal(EMPID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute EmpId.
     * @param value value to set the  EmpId
     */
    public void setEmpId(Number value) {
        setAttributeInternal(EMPID, value);
    }


    /**
     * Gets the attribute value for the calculated attribute EmpType.
     * @return the EmpType
     */
    public Number getEmpType() {
        return (Number) getAttributeInternal(EMPTYPE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute EmpType.
     * @param value value to set the  EmpType
     */
    public void setEmpType(Number value) {
        setAttributeInternal(EMPTYPE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Day.
     * @return the Day
     */
    public String getDay() {
        return (String) getAttributeInternal(DAY);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Day.
     * @param value value to set the  Day
     */
    public void setDay(String value) {
        setAttributeInternal(DAY, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Month.
     * @return the Month
     */
    public String getMonth() {
        return (String) getAttributeInternal(MONTH);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Month.
     * @param value value to set the  Month
     */
    public void setMonth(String value) {
        setAttributeInternal(MONTH, value);
    }

    /**
     * Gets the attribute value for the calculated attribute FinalMissed.
     * @return the FinalMissed
     */
    public String getFinalMissed() {
        String totalMissedHours = (String)getAttributeInternal(TOTALMISSED);
        String joiningDate = getJoiningDate(); // 01-APR-15 is the format
        String empType = "";
        if (getEmpType()!=null)
        {
            empType = getEmpType().toString();
        }
        if (joiningDate!=null && (empType.equals("2") || empType.equals("3")))
        {
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH)+1;
            int currentDay = calendar.get(Calendar.DATE);
            String []joiningDateArray = joiningDate.split("-");
            System.out.println(joiningDateArray);
            int joiningYear = Integer.parseInt("20"+joiningDateArray[2]);
            //int joiningMonth = Integer.parseInt(CommonUtil.getMonthNumber(joiningDateArray[1]));
            int joiningDay = Integer.parseInt(joiningDateArray[0]);
            int yearsDifference = currentYear - joiningYear;
            int monthsDifference = currentMonth - 1;//joiningMonth;
            int daysDifference = currentDay - joiningDay;
            boolean isUpToTwoYears;
            if (yearsDifference>2) 
            {
                isUpToTwoYears = false;    
            }
            else if (yearsDifference==2) 
            {
                if (monthsDifference>0)
                {
                    isUpToTwoYears = false;    
                }
                else if (monthsDifference==0) 
                {
                    if (daysDifference>0)
                    {
                        isUpToTwoYears = false;
                    }
                    else 
                    {
                        isUpToTwoYears = true;
                    }
                }
                else 
                {
                    isUpToTwoYears = true;
                }
            }
            else 
            {
                isUpToTwoYears = true;
            }
            String totalMissedTime = (String)getAttributeInternal(TOTALMISSED);
            String [] totalsArray = {};
            int hours = 0;
            int minutes = 0;
            totalsArray = totalMissedTime.split(":");
            if (totalsArray.length>0)
            {
                hours = Integer.parseInt(totalsArray[0]);
                minutes = Integer.parseInt(totalsArray[1]);
            }
            double totalMinutesBeforeWaiveOff = (hours*60)+minutes;
            finalMissedMinutesGlobal = (double)totalMinutesBeforeWaiveOff;
            double totalMinutesAfterWaiveOff = 0;
            double hoursAfterWaiveOff = 0;
            double minutesAfterWaiveOff = 0;
            if (empType.equals("2"))
            {
                if (isUpToTwoYears)
                {
                    totalMinutesAfterWaiveOff = totalMinutesBeforeWaiveOff - (totalMinutesBeforeWaiveOff*(25.0/100));
                    hoursAfterWaiveOff = totalMinutesAfterWaiveOff/60;
                    finalMissedMinutesGlobal = (double)totalMinutesAfterWaiveOff;
                    minutesAfterWaiveOff = totalMinutesAfterWaiveOff%60;
                }
                else 
                {
                    totalMinutesAfterWaiveOff = totalMinutesBeforeWaiveOff - (totalMinutesBeforeWaiveOff*(12.5/100));
                    hoursAfterWaiveOff = totalMinutesAfterWaiveOff/60;
                    finalMissedMinutesGlobal = (double)totalMinutesAfterWaiveOff;
                    minutesAfterWaiveOff = totalMinutesAfterWaiveOff%60;
                }
            }
            else if (empType.equals("3")) 
            {
                finalMissedMinutesGlobal = totalMinutesBeforeWaiveOff;
                totalMinutesAfterWaiveOff = totalMinutesBeforeWaiveOff;
                hoursAfterWaiveOff = totalMinutesAfterWaiveOff/60;
                minutesAfterWaiveOff = totalMinutesAfterWaiveOff%60;
            }
            return (int)hoursAfterWaiveOff +":"+(int)minutesAfterWaiveOff;
        }
        else return "";
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute FinalMissed.
     * @param value value to set the  FinalMissed
     */
    public void setFinalMissed(String value) {
        setAttributeInternal(FINALMISSED, value);
    }

    /**
     * Gets the attribute value for the calculated attribute CalculatedLeaves.
     * @return the CalculatedLeaves
     */
    public String getCalculatedLeaves() {
        if (finalMissedMinutesGlobal!=0)
        {
            return CommonUtil.round(finalMissedMinutesGlobal/(60*HOURS_PER_DAY),2)+"";
        }
        return finalMissedMinutesGlobal+"";
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute CalculatedLeaves.
     * @param value value to set the  CalculatedLeaves
     */
    public void setCalculatedLeaves(String value) {
        setAttributeInternal(CALCULATEDLEAVES, value);
    }

    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index,
                                           AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
        }
        return super.getAttrInvokeAccessor(index, attrDef);
    }

    /**
     * setAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param value the value to assign to the attribute
     * @param attrDef the attribute

     * @throws Exception
     */
    protected void setAttrInvokeAccessor(int index, Object value,
                                         AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }
}
