package vn.edu.crs.courseservice.service;

import vn.edu.crs.courseservice.dto.CourseDTO;
import vn.edu.crs.courseservice.entity.Course;
import vn.edu.crs.courseservice.repository.CourseRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // ENTITY -> DTO
    private CourseDTO toDTO(Course course) {

        return new CourseDTO(
                course.getId(),
                course.getTenMonHoc(),
                course.getSoTinChi(),
                course.getSoChoToiDa(),
                course.getSoChoConLai()
        );
    }

    // TÌM KIẾM + PHÂN TRANG
    public Page<CourseDTO> search(
            String keyword,
            Pageable pageable) {

        Page<Course> page;

        if (keyword == null || keyword.isBlank()) {

            page = courseRepository.findAll(pageable);

        } else {

            page = courseRepository
                    .findByTenMonHocContainingIgnoreCase(
                            keyword,
                            pageable
                    );
        }

        return page.map(this::toDTO);
    }

    // TRỪ 1 CHỖ
    @Transactional
    public CourseDTO reserveSeat(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Khong tim thay mon hoc"
                        ));

        if (course.getSoChoConLai() <= 0) {

            throw new IllegalStateException(
                    "Mon hoc da het cho"
            );
        }

        course.setSoChoConLai(
                course.getSoChoConLai() - 1
        );

        return toDTO(
                courseRepository.save(course)
        );
    }

    // HOÀN 1 CHỖ
    @Transactional
    public CourseDTO releaseSeat(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Khong tim thay mon hoc"
                        ));

        if (course.getSoChoConLai()
                < course.getSoChoToiDa()) {

            course.setSoChoConLai(
                    course.getSoChoConLai() + 1
            );
        }

        return toDTO(
                courseRepository.save(course)
        );
    }
}