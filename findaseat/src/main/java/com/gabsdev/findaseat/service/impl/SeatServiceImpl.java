package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.exception.*;
import com.gabsdev.findaseat.mapper.SeatMapper;
import com.gabsdev.findaseat.model.entity.Floor;
import com.gabsdev.findaseat.model.entity.Reservation;
import com.gabsdev.findaseat.model.entity.Seat;
import com.gabsdev.findaseat.model.enums.Status;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.repository.FloorsRepository;
import com.gabsdev.findaseat.repository.ReservationRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import com.gabsdev.findaseat.service.SeatService;
import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final FloorsRepository floorsRepository;
    private final BusinessRepository businessRepository;
    private final SeatMapper mapper;
    private final Slugify slugify;
    private  final ReservationRepository reservationRepository;

    public SeatServiceImpl(SeatRepository seatRepository,
                           FloorsRepository floorsRepository,
                           BusinessRepository businessRepository,
                           SeatMapper mapper, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.floorsRepository = floorsRepository;
        this.businessRepository = businessRepository;
        this.mapper = mapper;
        this.reservationRepository = reservationRepository;
        this.slugify = Slugify.builder().underscoreSeparator(true).build();
    }

    @Override
    public Seat createSeat(SeatRequest seatRequest) {
        verifyNumberofSeats(seatRequest);
        verifyFloorById(seatRequest.floorId());
        Floor floor = floorsRepository.findById(seatRequest.floorId())
                .orElseThrow(()-> new FloorNoFoundException("Seat Not found"));
        Seat seat = mapper.toSeat(seatRequest, floor);
        seat.setSeatName(slugify.slugify(seatRequest.type().name() + " " +
                seatRequest.number()));
        seat.setSlug(slugify.slugify(seat.getFloor().getSlug() + " " +
                seat.getSeatName()));
        verifiExistsSeat(seat);
        return seatRepository.save(seat);
    }

    private void verifyNumberofSeats(SeatRequest seatRequest) {
        if((seatRequest.numberOfSeats() == null || seatRequest.numberOfSeats()>1 ) &&
                (seatRequest.type().name().equalsIgnoreCase("seat") ||
                seatRequest.type().name().equalsIgnoreCase("desk") )){
            throw new NumberOfSeatsException("The number of seats most be 1 for a" + seatRequest.type().name());
        } else if ((seatRequest.numberOfSeats() == null || seatRequest.numberOfSeats()<2 )&&
                (seatRequest.type().name().equalsIgnoreCase("room") ||
                seatRequest.type().name().equalsIgnoreCase("table"))) {
            throw new NumberOfSeatsException("Is required define a number of seats bigger that 1 for a " + seatRequest.type().name());
        }
    }


    @Override
    public Seat getSeatById(UUID businessUuid, UUID id, LocalDate localDate) {
        verifyBusinessById(businessUuid);
        verifySeatById(id);
        return verifyReservation(seatRepository.findByIdAndFloor_BusinessUuid(id, businessUuid), localDate);
    }

    @Override
    public List<SeatResponse> getAllBusinessSeat(UUID businessUuid, LocalDate localDate) {
        verifyBusinessById(businessUuid);
        List<Seat> seatList = seatRepository.findByFloor_BusinessUuid(businessUuid);
        List<Seat> seats = seatList.stream().map(seat -> verifyReservation(seat, localDate)).toList();
        return seats.stream().map(mapper::toSeatResponse).toList();
    }

    private Seat verifyReservation(Seat seat, LocalDate localDate) {
        if (localDate == null){
            localDate = LocalDate.now();
        }
        if(reservationRepository.existsBySeat_IdAndReservationPeriod_reservationDay(seat.getId(), localDate)){
            List<Reservation> bySeatIdAndDateReservationDay = reservationRepository.findBySeat_IdAndReservationPeriod_reservationDay(seat.getId(), localDate);
            if (bySeatIdAndDateReservationDay.stream().anyMatch(Reservation::isActive)) {
                seat.setStatus(Status.RESERVED);
            }
        }
        return seat;
    }

    @Override
    public List<SeatResponse> getAllSeatSByFloor(UUID floorUuid, LocalDate localDate) {
        verifyFloorById(floorUuid);
        List<Seat> seatList = seatRepository.findByFloorId(floorUuid);
        List<Seat> seats = seatList.stream().map(seat -> verifyReservation(seat, localDate)).toList();
        return seats.stream().map(mapper::toSeatResponse).toList();
    }

    @Override
    public Seat updateSeat(Seat seat) {
        verifySeatById(seat.getId());
        return seatRepository.save(seat);
    }

    @Override
    public void deleteByBusinessIuudAndSeatId(UUID businessUuid, UUID id) {
        verifyBusinessById(businessUuid);
        verifySeatById(id);
        seatRepository.deleteById(id);
    }

    private void verifySeatById(UUID id) {
        if (!seatRepository.existsById(id)) {
            throw new SeatNotFoundException("Seat with id: " + id + " Not found");
        }
    }

    private void verifyFloorById(UUID floorId) {
        if (!floorsRepository.existsById(floorId)) {
            throw new FloorNoFoundException("Floor with id: " + floorId + " Not found");
        }
    }

    private void verifyBusinessById(UUID uuid) {
        if (!businessRepository.existsById(uuid)) {
            throw new BusinessNotFoundException("Business with id: " + uuid + " Not found");
        }
    }

    private void verifiExistsSeat(Seat seat) {
        if (seatRepository
                .existsBySeatNameAndFloorId(seat.getSeatName(),seat.getFloor().getId())
        ) {
            throw new SeatAlredyExistException("Seat already exist");
        }
    }

}
