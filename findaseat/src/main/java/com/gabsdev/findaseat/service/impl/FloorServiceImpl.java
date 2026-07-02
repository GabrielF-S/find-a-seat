package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.exception.BusinessNotFoundException;
import com.gabsdev.findaseat.exception.FloorAlredyExistException;
import com.gabsdev.findaseat.exception.FloorNoFoundException;
import com.gabsdev.findaseat.exception.SeatNotFoundException;
import com.gabsdev.findaseat.mapper.FloorMapper;
import com.gabsdev.findaseat.model.entity.Business;
import com.gabsdev.findaseat.model.entity.Floor;
import com.gabsdev.findaseat.model.enums.BusinessType;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.repository.FloorsRepository;
import com.gabsdev.findaseat.service.FloorService;
import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FloorServiceImpl implements FloorService {
    private final BusinessRepository businessRepository;
    private final FloorsRepository floorsRepository;
    private final FloorMapper mapper;
    private final Slugify slugify;


    public FloorServiceImpl(BusinessRepository businessRepository, FloorsRepository floorsRepository, FloorMapper mapper) {
        this.businessRepository = businessRepository;
        this.floorsRepository = floorsRepository;
        this.mapper = mapper;
        this.slugify = Slugify.builder().underscoreSeparator(true).build();
    }

    @Override
    public Floor creteFloor(FloorRequest request) {

        Business business = businessRepository.findById(request.businessId())
                .orElseThrow(() -> new BusinessNotFoundException("Business not found!"));

        String stringType;

        switch (business.getBusinessType()){
            case TRAVEL -> stringType = "- onibus";
            case BUSINESS -> stringType = "° andar";
            case RESTAURANT -> stringType= "- salao";
            default -> stringType= " " ;

        }


        verifyFloor(request, stringType);

        Floor floors = mapper.toFloor(request, stringType);

        floors.setBusiness(business);
        floors.setSlug(slugify.slugify(business.getBusinessName()+" " + request.towerName() + " " + floors.getFloorName()));
        return floorsRepository.save(floors);
    }

    private void verifyFloor(FloorRequest request, String stringType) {

        if (floorsRepository.existsByfloorNameAndBusinessUuidAndTowerName(request.floorNumber() + stringType, request.businessId(), request.towerName())) {
            throw new FloorAlredyExistException("Floor "+ request.floorNumber() +" Already exists");
        }
    }

    @Override
    public Floor getById(UUID uuid) {
        verifyById(uuid);
        return floorsRepository.findById(uuid).get();
    }

    @Override
    public Floor updateFloor(Floor floor) {
        verifyById(floor.getId());
        return floorsRepository.save(floor);
    }

    @Override
    public List<FloorResponse> getAll(UUID businessUuid) {
        verifyBusiness(businessUuid);
        List<Floor> byBusinessUuid = floorsRepository.findByBusinessUuid(businessUuid);
        return byBusinessUuid.stream().map(mapper::toFloorResponse).toList();
    }

    @Override
    public void deleteById(UUID uuid) {
        verifyById(uuid);
        floorsRepository.deleteById(uuid);

    }

    @Override
    public Floor insertLayout(UUID uuid, String layout) {
        if (!floorsRepository.existsById(uuid)){
            throw new SeatNotFoundException("Seat not found");
        }
        Floor floor = floorsRepository.findById(uuid).get();
        floor.setLayout(layout);
        return floorsRepository.save(floor);
    }

    public void verifyById(UUID uuid) {
        if (!floorsRepository.existsById(uuid)) {
            throw new FloorNoFoundException("Floor with id: " + uuid + " not Found");
        }
    }

    private void verifyBusiness(UUID businessUuid) {
        if (!businessRepository.existsById(businessUuid)){
            throw new BusinessNotFoundException("Business not found!");
        }
    }
}
