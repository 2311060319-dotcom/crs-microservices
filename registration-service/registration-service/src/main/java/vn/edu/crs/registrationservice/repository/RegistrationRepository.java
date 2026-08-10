package vn.edu.crs.registrationservice.repository;

import vn.edu.crs.registrationservice.entity.Registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    boolean existsByStudentIdAndCourseIdAndTrangThai(
            Long studentId,
            Long courseId,
            String trangThai
    );
}