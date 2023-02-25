package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.unit.UnitEntity;
import dev.thrako.receptaria.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {

    private UnitRepository unitRepository;

    public UnitService (UnitRepository unitRepository) {

        this.unitRepository = unitRepository;
    }

    public List<String> getDistinctUnitNames () {

        return this.unitRepository.findAllDistinctNames();
    }

    public UnitEntity getOrCreateByName (String name) {

        if (this.unitRepository.findUnitByName(name).isEmpty()) {
            final UnitEntity entity = new UnitEntity(name);
            unitRepository.saveAndFlush(entity);
        }

        return this.unitRepository.findUnitByName(name).get();
    }
}
