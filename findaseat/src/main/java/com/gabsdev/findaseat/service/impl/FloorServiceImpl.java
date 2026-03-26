package com.gabsdev.findaseat.service.impl;

import com.gabsdev.findaseat.dto.request.FloorRequest;
import com.gabsdev.findaseat.exception.BusinessNotFoundException;
import com.gabsdev.findaseat.exception.FloorNoFoundException;
import com.gabsdev.findaseat.mapper.FloorMapper;
import com.gabsdev.findaseat.model.Business;
import com.gabsdev.findaseat.model.Floor;
import com.gabsdev.findaseat.repository.BusinessRepository;
import com.gabsdev.findaseat.repository.FloorsRepository;
import com.gabsdev.findaseat.service.FloorService;
import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;

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
        if (!businessRepository.existsById(request.businessId())) {
            throw new BusinessNotFoundException("Business not found!");
        }
        Business business = businessRepository.findById(request.businessId()).get();
        Floor floors = mapper.toFloor(request);

        floors.setBusiness(business);
        floors.setSlug(slugify.slugify(business.getBusinessName() + " " + floors.getFloorName()));
        return floorsRepository.save(floors);
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
    public void deleteById(UUID uuid) {
        verifyById(uuid);
        floorsRepository.deleteById(uuid);

    }

    public void verifyById(UUID uuid) {
        if (!floorsRepository.existsById(uuid)) {
            throw new FloorNoFoundException("Floor with id: " + uuid + " not Found");
        }
    }
}
