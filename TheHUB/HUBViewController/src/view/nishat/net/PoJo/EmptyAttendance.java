package view.nishat.net.PoJo;

import java.util.Date;

public class EmptyAttendance {
    private String person_id;
    private String full_name;
    private Date attendance_date;
    private String max_start_time;
    private String max_end_time;
    private String start_time;
    private String end_time;
    private String expected_work_hours;
    
    public EmptyAttendance() {}

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setAttendance_date(Date attendance_date) {
        this.attendance_date = attendance_date;
    }

    public Date getAttendance_date() {
        return attendance_date;
    }

    public void setMax_start_time(String max_start_time) {
        this.max_start_time = max_start_time;
    }

    public String getMax_start_time() {
        return max_start_time;
    }

    public void setMax_end_time(String max_end_time) {
        this.max_end_time = max_end_time;
    }

    public String getMax_end_time() {
        return max_end_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setExpected_work_hours(String expected_work_hours) {
        this.expected_work_hours = expected_work_hours;
    }

    public String getExpected_work_hours() {
        return expected_work_hours;
    }
}
