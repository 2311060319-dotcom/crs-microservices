package vn.edu.crs.registrationservice.service;

import vn.edu.crs.registrationservice.client.CourseClient;
import vn.edu.crs.registrationservice.dto.RegistrationRequestDTO;
import vn.edu.crs.registrationservice.entity.Registration;
import vn.edu.crs.registrationservice.repository.RegistrationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final String DA_DANG_KY = "DA_DANG_KY";
    private static final String DA_HUY = "DA_HUY";

    private final RegistrationRepository registrationRepository;

    private final CourseClient courseClient;

    // =========================
    // ĐĂNG KÝ MÔN HỌC
    // =========================
    public Registration register(
            RegistrationRequestDTO dto) {

        // Kiểm tra sinh viên đã đăng ký chưa
        boolean exists =
                registrationRepository
                        .existsByStudentIdAndCourseIdAndTrangThai(
                                dto.getStudentId(),
                                dto.getCourseId(),
                                DA_DANG_KY
                        );

        if (exists) {
            throw new IllegalStateException(
                    "Sinh vien da dang ky mon hoc nay roi"
            );
        }

        // Gọi Course Service để trừ chỗ
        courseClient.reserveSeat(
                dto.getCourseId()
        );

        // Tạo đăng ký
        Registration registration =
                new Registration();

        registration.setStudentId(
                dto.getStudentId()
        );

        registration.setCourseId(
                dto.getCourseId()
        );

        registration.setTrangThai(
                DA_DANG_KY
        );

        registration.setNgayDangKy(
                LocalDateTime.now()
        );

        return registrationRepository.save(
                registration
        );
    }

    // =========================
    // HỦY ĐĂNG KÝ
    // =========================
    public void cancel(Long id) {

        Registration registration =
                registrationRepository.findById(id)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "Khong tim thay dang ky"
                                ));

        // Đã hủy rồi
        if (DA_HUY.equals(
                registration.getTrangThai())) {

            throw new IllegalStateException(
                    "Dang ky da duoc huy"
            );
        }

        // Gọi Course Service hoàn chỗ
        courseClient.releaseSeat(
                registration.getCourseId()
        );

        // Đổi trạng thái
        registration.setTrangThai(
                DA_HUY
        );

        registrationRepository.save(
                registration
        );
    }
}