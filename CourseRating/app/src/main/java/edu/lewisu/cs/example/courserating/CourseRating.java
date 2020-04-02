package edu.lewisu.cs.example.courserating;



public class CourseRating {
    private String courseName;
    private String instructorName;
    private String courseType;
    private int rating;

    public CourseRating() {
        courseName = "";
        instructorName="";
        courseType="";
        rating=0;
    }

    public CourseRating(String courseName, String instructorName, String courseType, int rating) {
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.courseType = courseType;
        this.rating = rating;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return  "courseName=" + courseName + "\n" +
                "instructorName=" + instructorName + "\n" +
                "courseType=" + courseType + "\n" +
                "rating=" + rating ;
    }
}
