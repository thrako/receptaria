package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.entity.ingredient.IngredientEntity;
import dev.thrako.receptaria.model.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService (IngredientRepository ingredientRepository) {

        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientEntity> saveAllAndFlush (List<IngredientEntity> ingredientEntities) {

        return this.ingredientRepository.saveAllAndFlush(ingredientEntities);
    }
}
