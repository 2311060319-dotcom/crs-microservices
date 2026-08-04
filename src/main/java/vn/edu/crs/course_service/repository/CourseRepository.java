package vn.edu.crs.course_service.repository;

import vn.edu.crs.course_service.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Kiểm tra xem môn học đã tồn tại chưa (bỏ qua viết hoa/thường)
    boolean existsByTenMonHocIgnoreCase(String tenMonHoc);
}