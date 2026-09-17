package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.dto.response.FloorResponse;
import com.gabsdev.findaseat.dto.response.LayoutResponse;
import com.gabsdev.findaseat.exception.BusinessNotFoundException;
import com.gabsdev.findaseat.exception.FloorAlredyExistException;
import com.gabsdev.findaseat.exception.FloorNoFoundException;
import com.gabsdev.findaseat.mapper.FloorMapper;
import com.gabsdev.findaseat.model.entity.Business;
import com.gabsdev.findaseat.model.entity.Floor;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.repository.FloorsRepository;
import com.gabsdev.findaseat.service.FloorService;
import com.github.slugify.Slugify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public FloorResponse getById(UUID uuid) {
        verifyById(uuid);
        return mapper.toFloorResponse(floorsRepository.findById(uuid).get());
    }

    @Override
    public FloorResponse updateFloor(Floor floor) {
        verifyById(floor.getId());
        return mapper.toFloorResponse(floorsRepository.save(floor));
    }

    @Override
    public Page<FloorResponse> getAll(UUID businessUuid, Integer page, Integer size) {
        verifyBusiness(businessUuid);
        Sort sort = Sort.by(Sort.Direction.ASC, "floorName");
        PageRequest pageRequest = PageRequest.of(page,size, sort);
        Page<Floor> byBusinessUuid = floorsRepository.findByBusinessUuid(businessUuid, pageRequest);
        return byBusinessUuid.map(mapper::toFloorResponse);
    }

    @Override
    public void deleteById(UUID uuid) {
        verifyById(uuid);
        floorsRepository.deleteById(uuid);
    }

    @Override
    public FloorResponse insertLayout(UUID uuid, String layout) {
        Floor floor = floorsRepository.findById(uuid)
                .orElseThrow(() -> new FloorNoFoundException("Floor not found"));
        floor.setLayout(layout);
        return mapper.toFloorResponse(floorsRepository.save(floor));
    }

    @Override
    public LayoutResponse getLayoutByUuid(UUID uuid) {
        Floor floor = floorsRepository.findById(uuid).orElseThrow(() -> new FloorNoFoundException("Floor not found"));
        return new LayoutResponse(floor.getLayout());
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