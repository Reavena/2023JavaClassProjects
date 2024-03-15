/*
 * @author Martynas Šalčiūnas 2 kursas 2 grupė informatika
 */
//Gabija Liorentaite
//3 grupe Informatika 2 kursas


package com.example.studentattendence;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

interface StudentInterface {
    void setID(String n);
    void setGroup(String str);
    String getID();
    String getGroup();
}

interface DateInterface {
    void setDate(LocalDate date);
    LocalDate getDate();
}

class SystemStudent implements StudentInterface {
    private String ID;
    private String group;

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemStudent that = (SystemStudent) o;
        return Objects.equals(ID, that.ID) && Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, group);
    }
}
abstract class Person implements StudentInterface, DateInterface {
    protected SimpleStringProperty ID;
    protected SimpleObjectProperty<LocalDate> date;
    protected SimpleStringProperty group;

    public Person(String ID, String group, LocalDate date) {
        this.ID = new SimpleStringProperty(ID);
        this.date = new SimpleObjectProperty<>(date);
        this.group = new SimpleStringProperty(group);
    }

    @Override
    public String getID() {
        return ID.get();
    }

    @Override
    public LocalDate getDate() {
        return date.get();
    }

    @Override
    public String getGroup() {
        return group.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(ID.get(), person.ID.get()) && Objects.equals(date.get(), person.date.get()) && Objects.equals(group.get(), person.group.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID.get(), date.get(), group.get());
    }
}

class Student extends Person {
    private SimpleBooleanProperty attendance;

    public Student(String ID, String group, boolean attendance, LocalDate date) {
        super(ID, group, date);
        this.attendance = new SimpleBooleanProperty(attendance);
    }

    public boolean getAttendance() {
        return attendance.get();
    }

    public void setAttendance(boolean attendance) {
        this.attendance.set(attendance);
    }

    public void printStudent() {
        System.out.println(date + " " + ID);
    }

    public SimpleBooleanProperty attendanceProperty() {
        return attendance;
    }
    public SimpleStringProperty IDProperty() {
        return ID;
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public SimpleStringProperty groupProperty() {
        return group;
    }
}

class StudentSingleton {

    private static StudentSingleton instance;

    private Set<Student> studentList;

    private StudentSingleton() {

    }
    private ObservableList<Student> results = FXCollections.observableArrayList();



    public void setResults(Set<Student> stud) {
        results.clear();
        for(Student temp : stud)
        {
            results.add(temp);
        }
    }

    public ObservableList<Student> getResults() {
        return results;
    }

    public static StudentSingleton getInstance() {
        if (instance == null) {
            instance = new StudentSingleton();
        }
        return instance;
    }

    public Set<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(Set<Student> studentList) {
        this.studentList = studentList;
    }

    public void addStudent(Student student) {
        this.studentList.add(student);
    }

    public void addGroup(Student student) {
        String group = student.getGroup();
        LocalDate date = student.getDate();
        boolean attendance = student.getAttendance();
        for(SystemStudent stud : GroupSingleton.getInstance().getStudentList()) {
            if(stud.getGroup().equals(group))
            {
                Student newStudent = new Student(stud.getID(), group, attendance, date);

                addStudent(newStudent);
            }
        }
    }

    public void removeStudent(Student student) {
        this.studentList.remove(student);
    }

    public void removeGroup(Student student) {

        for(Student stud : this.studentList.toArray(new Student[]{})) {
            if(stud.getGroup().equals(student.getGroup()) &&
                    stud.getDate().equals(student.getDate())) {
                removeStudent(stud);
            }
        }
    }
}

class GroupSingleton {

    private static GroupSingleton instance;

    private Set<SystemStudent> studentList;

    private GroupSingleton() {

    }

    public static GroupSingleton getInstance() {
        if (instance == null) {
            instance = new GroupSingleton();
        }
        return instance;
    }

    public Set<SystemStudent> getStudentList() {
        return studentList;
    }

    public void setStudentList(Set<SystemStudent> studentList) {
        this.studentList = studentList;
    }

    public void addStudent(SystemStudent student) {
        for(SystemStudent stud : studentList) {
            if(student.getID().equals(stud.getID()) && student.getGroup().equals(stud.getGroup()))
                return;
        }
        this.studentList.add(student);
    }

    public void removeStudent(SystemStudent student) {
        this.studentList.remove(student);
    }
}

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 704, 315);
        stage.setTitle("Student regulation system");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        Set<SystemStudent> groupList = new HashSet<SystemStudent>();
        GroupSingleton.getInstance().setStudentList(groupList);

        Set<Student> studentList  = new HashSet<Student>();
        StudentSingleton.getInstance().setStudentList(studentList);

        launch();
    }
}