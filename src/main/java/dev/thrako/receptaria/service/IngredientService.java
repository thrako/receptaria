package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.ingredient.IngredientEntity;
import dev.thrako.receptaria.repository.IngredientRepository;
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
