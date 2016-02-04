package hub.nishat.net.model.VO.HR;

import oracle.jbo.RowSet;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Jun 16 16:34:48 PKT 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class VO_EmpFlexiRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        HoursFlexible {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getHoursFlexible();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setHoursFlexible((String)value);
            }
        }
        ,
        Mins {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getMins();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setMins((String)value);
            }
        }
        ,
        WorkingTime {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getWorkingTime();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setWorkingTime((String)value);
            }
        }
        ,
        EffectiveEndDate {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEffectiveEndDate();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setEffectiveEndDate((Date)value);
            }
        }
        ,
        EffectiveStartDate {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEffectiveStartDate();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setEffectiveStartDate((Date)value);
            }
        }
        ,
        EmpId {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEmpId();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setEmpId((String)value);
            }
        }
        ,
        EndTime {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEndTime();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setEndTime((String)value);
            }
        }
        ,
        ExpectedWorkHours {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getExpectedWorkHours();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setExpectedWorkHours((String)value);
            }
        }
        ,
        FlexiId {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getFlexiId();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setFlexiId((Number)value);
            }
        }
        ,
        MaxEndTime {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getMaxEndTime();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setMaxEndTime((String)value);
            }
        }
        ,
        MaxStartTime {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getMaxStartTime();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setMaxStartTime((String)value);
            }
        }
        ,
        StartTime {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getStartTime();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setStartTime((String)value);
            }
        }
        ,
        PersonId {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getPersonId();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setPersonId((Number)value);
            }
        }
        ,
        EmpTypeId {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEmpTypeId();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setEmpTypeId((Number)value);
            }
        }
        ,
        EmployeeType {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEmployeeType();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setEmployeeType((String)value);
            }
        }
        ,
        FullNameChild {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getFullNameChild();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setFullNameChild((String)value);
            }
        }
        ,
        HierarchyName {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getHierarchyName();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setHierarchyName((String)value);
            }
        }
        ,
        EmployeeLov1 {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEmployeeLov1();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        DepartmentLov1 {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getDepartmentLov1();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        EmpTypeLov1 {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getEmpTypeLov1();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        FlexiHoursLov1 {
            public Object get(VO_EmpFlexiRowImpl obj) {
                return obj.getFlexiHoursLov1();
            }

            public void put(VO_EmpFlexiRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(VO_EmpFlexiRowImpl object);

        public abstract void put(VO_EmpFlexiRowImpl object, Object value);

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


    public static final int HOURSFLEXIBLE = AttributesEnum.HoursFlexible.index();
    public static final int MINS = AttributesEnum.Mins.index();
    public static final int WORKINGTIME = AttributesEnum.WorkingTime.index();
    public static final int EFFECTIVEENDDATE = AttributesEnum.EffectiveEndDate.index();
    public static final int EFFECTIVESTARTDATE = AttributesEnum.EffectiveStartDate.index();
    public static final int EMPID = AttributesEnum.EmpId.index();
    public static final int ENDTIME = AttributesEnum.EndTime.index();
    public static final int EXPECTEDWORKHOURS = AttributesEnum.ExpectedWorkHours.index();
    public static final int FLEXIID = AttributesEnum.FlexiId.index();
    public static final int MAXENDTIME = AttributesEnum.MaxEndTime.index();
    public static final int MAXSTARTTIME = AttributesEnum.MaxStartTime.index();
    public static final int STARTTIME = AttributesEnum.StartTime.index();
    public static final int PERSONID = AttributesEnum.PersonId.index();
    public static final int EMPTYPEID = AttributesEnum.EmpTypeId.index();
    public static final int EMPLOYEETYPE = AttributesEnum.EmployeeType.index();
    public static final int FULLNAMECHILD = AttributesEnum.FullNameChild.index();
    public static final int HIERARCHYNAME = AttributesEnum.HierarchyName.index();
    public static final int EMPLOYEELOV1 = AttributesEnum.EmployeeLov1.index();
    public static final int DEPARTMENTLOV1 = AttributesEnum.DepartmentLov1.index();
    public static final int EMPTYPELOV1 = AttributesEnum.EmpTypeLov1.index();
    public static final int FLEXIHOURSLOV1 = AttributesEnum.FlexiHoursLov1.index();

    /**
     * This is the default constructor (do not remove).
     */
    public VO_EmpFlexiRowImpl() {
    }

    /**
     * Gets EO_EmpFlexi entity object.
     * @return the EO_EmpFlexi
     */
    public EntityImpl getEO_EmpFlexi() {
        return (EntityImpl)getEntity(0);
    }

    /**
     * Gets the attribute value for the calculated attribute HoursFlexible.
     * @return the HoursFlexible
     */
    public String getHoursFlexible() {
        return (String) getAttributeInternal(HOURSFLEXIBLE);
        //return (String) getAttributeInternal(HOURSFLEXIBLE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute HoursFlexible.
     * @param value value to set the  HoursFlexible
     */
    public void setHoursFlexible(String value) {
        setAttributeInternal(HOURSFLEXIBLE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Mins.
     * @return the Mins
     */
    public String getMins() {
        return (String) getAttributeInternal(MINS);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Mins.
     * @param value value to set the  Mins
     */
    public void setMins(String value) {
        setAttributeInternal(MINS, value);
    }

    /**
     * Gets the attribute value for the calculated attribute WorkingTime.
     * @return the WorkingTime
     */
    public String getWorkingTime() {
        return (String) getAttributeInternal(WORKINGTIME);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute WorkingTime.
     * @param value value to set the  WorkingTime
     */
    public void setWorkingTime(String value) {
        setAttributeInternal(WORKINGTIME, value);
    }

    /**
     * Gets the attribute value for EFFECTIVE_END_DATE using the alias name EffectiveEndDate.
     * @return the EFFECTIVE_END_DATE
     */
    public Date getEffectiveEndDate() {
        return (Date) getAttributeInternal(EFFECTIVEENDDATE);
    }

    /**
     * Sets <code>value</code> as attribute value for EFFECTIVE_END_DATE using the alias name EffectiveEndDate.
     * @param value value to set the EFFECTIVE_END_DATE
     */
    public void setEffectiveEndDate(Date value) {
        setAttributeInternal(EFFECTIVEENDDATE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute EffectiveStartDate.
     * @return the EffectiveStartDate
     */
    public Date getEffectiveStartDate() {
        return (Date) getAttributeInternal(EFFECTIVESTARTDATE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute EffectiveStartDate.
     * @param value value to set the  EffectiveStartDate
     */
    public void setEffectiveStartDate(Date value) {
        setAttributeInternal(EFFECTIVESTARTDATE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute EmpId.
     * @return the EmpId
     */
    public String getEmpId() {
        return (String) getAttributeInternal(EMPID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute EmpId.
     * @param value value to set the  EmpId
     */
    public void setEmpId(String value) {
        setAttributeInternal(EMPID, value);
    }

    /**
     * Gets the attribute value for END_TIME using the alias name EndTime.
     * @return the END_TIME
     */
    public String getEndTime() {
        return (String) getAttributeInternal(ENDTIME);
    }

    /**
     * Sets <code>value</code> as attribute value for END_TIME using the alias name EndTime.
     * @param value value to set the END_TIME
     */
    public void setEndTime(String value) {
        setAttributeInternal(ENDTIME, value);
    }

    /**
     * Gets the attribute value for the calculated attribute ExpectedWorkHours.
     * @return the ExpectedWorkHours
     */
    public String getExpectedWorkHours() {
        return (String) getAttributeInternal(EXPECTEDWORKHOURS);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute ExpectedWorkHours.
     * @param value value to set the  ExpectedWorkHours
     */
    public void setExpectedWorkHours(String value) {
        setAttributeInternal(EXPECTEDWORKHOURS, value);
    }

    /**
     * Gets the attribute value for FLEXI_ID using the alias name FlexiId.
     * @return the FLEXI_ID
     */
    public Number getFlexiId() {
        return (Number) getAttributeInternal(FLEXIID);
    }

    /**
     * Sets <code>value</code> as attribute value for FLEXI_ID using the alias name FlexiId.
     * @param value value to set the FLEXI_ID
     */
    public void setFlexiId(Number value) {
        setAttributeInternal(FLEXIID, value);
    }

    /**
     * Gets the attribute value for MAX_END_TIME using the alias name MaxEndTime.
     * @return the MAX_END_TIME
     */
    public String getMaxEndTime() {
        return (String) getAttributeInternal(MAXENDTIME);
    }

    /**
     * Sets <code>value</code> as attribute value for MAX_END_TIME using the alias name MaxEndTime.
     * @param value value to set the MAX_END_TIME
     */
    public void setMaxEndTime(String value) {
        setAttributeInternal(MAXENDTIME, value);
    }

    /**
     * Gets the attribute value for MAX_START_TIME using the alias name MaxStartTime.
     * @return the MAX_START_TIME
     */
    public String getMaxStartTime() {
        return (String) getAttributeInternal(MAXSTARTTIME);
    }

    /**
     * Sets <code>value</code> as attribute value for MAX_START_TIME using the alias name MaxStartTime.
     * @param value value to set the MAX_START_TIME
     */
    public void setMaxStartTime(String value) {
        setAttributeInternal(MAXSTARTTIME, value);
    }

    /**
     * Gets the attribute value for START_TIME using the alias name StartTime.
     * @return the START_TIME
     */
    public String getStartTime() {
        return (String) getAttributeInternal(STARTTIME);
    }

    /**
     * Sets <code>value</code> as attribute value for START_TIME using the alias name StartTime.
     * @param value value to set the START_TIME
     */
    public void setStartTime(String value) {
        setAttributeInternal(STARTTIME, value);
    }

    /**
     * Gets the attribute value for the calculated attribute PersonId.
     * @return the PersonId
     */
    public Number getPersonId() {
        return (Number) getAttributeInternal(PERSONID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute PersonId.
     * @param value value to set the  PersonId
     */
    public void setPersonId(Number value) {
        setAttributeInternal(PERSONID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute EmpTypeId.
     * @return the EmpTypeId
     */
    public Number getEmpTypeId() {
        return (Number) getAttributeInternal(EMPTYPEID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute EmpTypeId.
     * @param value value to set the  EmpTypeId
     */
    public void setEmpTypeId(Number value) {
        setAttributeInternal(EMPTYPEID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute EmployeeType.
     * @return the EmployeeType
     */
    public String getEmployeeType() {
        return (String) getAttributeInternal(EMPLOYEETYPE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute EmployeeType.
     * @param value value to set the  EmployeeType
     */
    public void setEmployeeType(String value) {
        setAttributeInternal(EMPLOYEETYPE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute FullNameChild.
     * @return the FullNameChild
     */
    public String getFullNameChild() {
        return (String) getAttributeInternal(FULLNAMECHILD);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute FullNameChild.
     * @param value value to set the  FullNameChild
     */
    public void setFullNameChild(String value) {
        setAttributeInternal(FULLNAMECHILD, value);
    }

    /**
     * Gets the attribute value for the calculated attribute HierarchyName.
     * @return the HierarchyName
     */
    public String getHierarchyName() {
        return (String) getAttributeInternal(HIERARCHYNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute HierarchyName.
     * @param value value to set the  HierarchyName
     */
    public void setHierarchyName(String value) {
        setAttributeInternal(HIERARCHYNAME, value);
    }

    /**
     * Gets the view accessor <code>RowSet</code> EmployeeLov1.
     */
    public RowSet getEmployeeLov1() {
        return (RowSet)getAttributeInternal(EMPLOYEELOV1);
    }

    /**
     * Gets the view accessor <code>RowSet</code> DepartmentLov1.
     */
    public RowSet getDepartmentLov1() {
        return (RowSet)getAttributeInternal(DEPARTMENTLOV1);
    }

    /**
     * Gets the view accessor <code>RowSet</code> EmpTypeLov1.
     */
    public RowSet getEmpTypeLov1() {
        return (RowSet)getAttributeInternal(EMPTYPELOV1);
    }

    /**
     * Gets the view accessor <code>RowSet</code> FlexiHoursLov1.
     */
    public RowSet getFlexiHoursLov1() {
        return (RowSet)getAttributeInternal(FLEXIHOURSLOV1);
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
