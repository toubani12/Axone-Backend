package com.axone.database.controller;

import com.axone.database.model.Student;
import com.axone.database.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return studentService.getStudentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student studentDetails) {
        return studentService.getStudentById(id)
            .map(student -> {
                student.setName(studentDetails.getName());
                student.setLastname(studentDetails.getLastname());
                student.setEmail(studentDetails.getEmail());
                student.setPassword(studentDetails.getPassword());
                student.setCity(studentDetails.getCity());
                student.setStudentClass(studentDetails.getStudentClass());
                student.setBiblio(studentDetails.getBiblio());
                return ResponseEntity.ok(studentService.saveStudent(student));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> partialUpdateStudent(@PathVariable int id, @RequestBody Student studentDetails) {
        return studentService.getStudentById(id)
            .map(student -> {
                if (studentDetails.getName() != null) {
                    student.setName(studentDetails.getName());
                }
                if (studentDetails.getLastname() != null) {
                    student.setLastname(studentDetails.getLastname());
                }
                if (studentDetails.getEmail() != null) {
                    student.setEmail(studentDetails.getEmail());
                }
                if (studentDetails.getPassword() != null) {
                    student.setPassword(studentDetails.getPassword());
                }
                if (studentDetails.getCity() != null) {
                    student.setCity(studentDetails.getCity());
                }
                if (studentDetails.getStudentClass() != null) {
                    student.setStudentClass(studentDetails.getStudentClass());
                }
                if (studentDetails.getBiblio() != null) {
                    student.setBiblio(studentDetails.getBiblio());
                }
                return ResponseEntity.ok(studentService.saveStudent(student));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        if (studentService.getStudentById(id).isPresent()) {
            studentService.deleteStudent(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
