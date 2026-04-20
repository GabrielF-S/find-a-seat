package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.SeatRequest;
import com.gabsdev.findaseat.dto.response.SeatResponse;
import com.gabsdev.findaseat.exception.BusinessNotFoundException;
import com.gabsdev.findaseat.exception.FloorNoFoundException;
import com.gabsdev.findaseat.exception.SeatAlredyExistException;
import com.gabsdev.findaseat.exception.SeatNotFoundException;
import com.gabsdev.findaseat.mapper.SeatMapper;
import com.gabsdev.findaseat.model.Floor;
import com.gabsdev.findaseat.model.Seat;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.repository.FloorsRepository;
import com.gabsdev.findaseat.repository.SeatRepository;
import com.gabsdev.findaseat.service.SeatService;
import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final FloorsRepository floorsRepository;
    private final BusinessRepository businessRepository;
    private final SeatMapper mapper;
    private final Slugify slugify;

    public SeatServiceImpl(SeatRepository seatRepository,
                           FloorsRepository floorsRepository,
                           BusinessRepository businessRepository,
                           SeatMapper mapper) {
        this.seatRepository = seatRepository;
        this.floorsRepository = floorsRepository;
        this.businessRepository = businessRepository;
        this.mapper = mapper;
        this.slugify = Slugify.builder().underscoreSeparator(true).build();
    }

    @Override
    public Seat createSeat(SeatRequest seatRequest) {
        verifyFloorById(seatRequest.floorId());
        Floor floor = floorsRepository.findById(seatRequest.floorId()).get();
        Seat seat = mapper.toSeat(seatRequest, floor);
        seat.setSeatName(slugify.slugify(seatRequest.type().name() + " " +
                seatRequest.number()));
        seat.setSlug(slugify.slugify(seat.getFloors().getSlug() + " " +
                seat.getSeatName()));
        verifiExistsSeat(seat);
        return seatRepository.save(seat);
    }


    @Override
    public Seat getSeatById(UUID businessUuid, Long id) {
        verifyBusinessById(businessUuid);
        verifySeatById(id);
        Seat seat = seatRepository.findByIdAndFloor_BusinessUuid(id, businessUuid);
        seat.getFloors().getBusiness().getUuid();
        return seat;
    }

    @Override
    public List<SeatResponse> getAllBusinessSeat(UUID businessUuid) {
        verifyBusinessById(businessUuid);
        List<Seat> seatList = seatRepository.findByFloor_BusinessUuid(businessUuid);
        return seatList.stream().map(mapper::toSeatResponse).toList();
    }

    @Override
    public List<SeatResponse> getAllSeatSByFloor(UUID floorUuid) {
        verifyFloorById(floorUuid);
        List<Seat> seatList = seatRepository.findByFloorId(floorUuid);
        return seatList.stream().map(mapper::toSeatResponse).toList();
    }

    @Override
    public Seat updateSeat(Seat seat) {
        verifySeatById(seat.getId());
        return seatRepository.save(seat);
    }

    @Override
    public void deleteByBusinessIuudAndSeatId(UUID businessUuid, Long id) {
        verifyBusinessById(businessUuid);
        verifySeatById(id);
        seatRepository.deleteById(id);
    }

    private void verifySeatById(Long id) {
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
                .existsBySeatNameAndFloorId(seat.getSeatName(),seat.getFloors().getId())
        ) {
            throw new SeatAlredyExistException("Seat already exist");
        }
    }
}
