package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.ingredient.dto.IngredientDTO;
import dev.thrako.receptaria.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IngredientService {

    IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService (IngredientRepository ingredientRepository) {

        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDTO> getIngredientDtoList (Map<String, String[]> parameterMap) {

        List<IngredientDTO> ingredientDTOS = new ArrayList<>();
        final String[] names = parameterMap.get("ingredientName");
        final String[] quantities = parameterMap.get("ingredientQuantity");
        final String[] units = parameterMap.get("ingredientUnit");

        for (int i = 0; i < names.length; i++) {

            if ((names[i]).isBlank() && quantities[i].isBlank() && units[i].isBlank()) {
                continue;
            }

            ingredientDTOS.add(new IngredientDTO()
                    .setIngredientName(names[i])
                    .setQuantity(quantities[i])
                    .setUnitName(units[i]));
        }

        return ingredientDTOS;
    }


}
