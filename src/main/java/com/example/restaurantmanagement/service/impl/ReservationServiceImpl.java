package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.Actor;
import com.example.restaurantmanagement.entity.DiningTable;
import com.example.restaurantmanagement.entity.Reservation;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.ReservationMapper;
import com.example.restaurantmanagement.repository.ActorRepository;
import com.example.restaurantmanagement.repository.DiningTableRepository;
import com.example.restaurantmanagement.repository.ReservationRepository;
import com.example.restaurantmanagement.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final DiningTableRepository tableRepository;
    private final ActorRepository actorRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        DiningTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));

        // Kiểm tra bàn có đang bảo trì không
        if (table.getStatus() == TableStatus.MAINTENANCE)
            throw new AppException(ErrorCode.TABLE_NOT_AVAILABLE);

        // Kiểm tra xung đột giờ đặt (±2 giờ)
        LocalDateTime from = request.getReservationTime().minusHours(2);
        LocalDateTime to   = request.getReservationTime().plusHours(2);
        if (reservationRepository.existsConflictingReservation(request.getTableId(), from, to))
            throw new AppException(ErrorCode.RESERVATION_CONFLICT);

        // Lấy Actor hiện tại đang đăng nhập làm createdBy
        Actor createdBy = getCurrentActor();

        // Nếu có customerId thì load Actor khách hàng
        Actor customer = null;
        if (request.getCustomerId() != null) {
            customer = actorRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));
        }

        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setDiningTable(table);
        reservation.setCustomer(customer);
        reservation.setCreatedBy(createdBy);
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getById(Long id) {
        return reservationMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getAll(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(reservationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> search(LocalDateTime from, LocalDateTime to,
                                             ReservationStatus status, Pageable pageable) {
        return reservationRepository.findByDateRangeAndStatus(from, to, status, pageable)
                .map(reservationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getByCustomer(Long customerId, Pageable pageable) {
        return reservationRepository.findByCustomerId(customerId, pageable)
                .map(reservationMapper::toResponse);
    }

    @Override
    @Transactional
    public ReservationResponse confirm(Long id) {
        Reservation reservation = findById(id);
        if (reservation.getStatus() != ReservationStatus.PENDING)
            throw new AppException(ErrorCode.RESERVATION_CANNOT_CANCEL,
                    "Chỉ có thể xác nhận đặt bàn ở trạng thái chờ xác nhận");

        reservation.setStatus(ReservationStatus.CONFIRMED);
        // Cập nhật trạng thái bàn sang đã đặt
        reservation.getDiningTable().setStatus(TableStatus.RESERVED);
        tableRepository.save(reservation.getDiningTable());

        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse cancel(Long id) {
        Reservation reservation = findById(id);
        if (reservation.getStatus() == ReservationStatus.CANCELLED)
            throw new AppException(ErrorCode.RESERVATION_CANNOT_CANCEL);

        reservation.setStatus(ReservationStatus.CANCELLED);
        // Nếu bàn đang ở trạng thái đã đặt thì trả về trống
        if (reservation.getDiningTable().getStatus() == TableStatus.RESERVED) {
            reservation.getDiningTable().setStatus(TableStatus.AVAILABLE);
            tableRepository.save(reservation.getDiningTable());
        }

        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    // ---- helpers ----
    private Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    private Actor getCurrentActor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new AppException(ErrorCode.UNAUTHORIZED);
        return actorRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));
    }
}

