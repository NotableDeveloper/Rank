package NotableDeveloper.rank.domain.enums;

public enum Semester {
    FIRST("1학기"),
    SECOND("2학기");

    private String semester;

    Semester(String semester){
        this.semester = semester;
    }

    public String getSemester(){
        return this.semester;
    }
}
