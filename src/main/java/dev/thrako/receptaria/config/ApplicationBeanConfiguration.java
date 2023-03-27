package dev.thrako.receptaria.config;

import dev.thrako.receptaria.model.ingredient.IngredientEntity;
import dev.thrako.receptaria.model.ingredient.dto.IngredientDTO;
import dev.thrako.receptaria.model.photo.PhotoEntity;
import dev.thrako.receptaria.model.photo.dto.PhotoBM;
import dev.thrako.receptaria.model.recipe.RecipeEntity;
import dev.thrako.receptaria.model.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.model.user.dto.UserLoginBM;
import dev.thrako.receptaria.service.IngredientNameService;
import dev.thrako.receptaria.service.UnitService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ApplicationBeanConfiguration {

    private final IngredientNameService ingredientNameService;
    private final UnitService unitService;

    public ApplicationBeanConfiguration (IngredientNameService ingredientNameService,
                                         UnitService unitService) {

        this.ingredientNameService = ingredientNameService;
        this.unitService = unitService;
    }

    @Bean
    @Qualifier("userMapper")
    public ModelMapper userMapper (PasswordEncoder passwordEncoder) {

        ModelMapper mapper = new ModelMapper();

        Converter<String, String> passwordHash = ctx -> ctx.getSource() == null ? null :
                passwordEncoder.encode(ctx.getSource());

        mapper.createTypeMap(UserLoginBM.class, UserEntity.class)
                .addMappings(mpr -> mpr.using(passwordHash).map(UserLoginBM::getPassword, UserEntity::setPassword));

        return mapper;
    }

    @Bean
    @Qualifier("recipeMapper")
    public ModelMapper recipeMapper () {

        ModelMapper recipeMapper = new ModelMapper();

        final Function<PhotoBM, PhotoEntity> photoDtoToEntity = photoDTO -> {
            PhotoEntity entity = new PhotoEntity();

//            if (UploadUtility.uploadPhoto(photoDTO)) {
//                return entity.setUrl(photoDTO.getUrl());
//            }

            //TODO implement with exception
            return null;
        };

        Converter<List<PhotoBM>, List<PhotoEntity>> photoConverter = ctx -> (ctx.getSource() == null)
                    ? new ArrayList<>()
                    : ctx.getSource()
                        .stream()
                        .map(photoDtoToEntity)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        Converter<List<IngredientDTO>, List<IngredientEntity>> ingredientConverter = ctx -> (ctx.getSource() == null)
                    ? new ArrayList<>()
                    : ctx.getSource()
                        .stream()
                        .map(dto -> new IngredientEntity()
                                        .setIngredientName(ingredientNameService.getOrCreateByName(dto.getIngredientName()))
                                        .setQuantity(dto.getQuantity())
                                        .setUnit(unitService.getOrCreateByName(dto.getUnitName())))
                        .collect(Collectors.toCollection(ArrayList::new));


        recipeMapper.createTypeMap(RecipeBM.class, RecipeEntity.class)
                .addMappings(mpr -> mpr.map(RecipeBM::getTitle, RecipeEntity::setTitle))
//                .addMappings(mpr -> mpr.using(photoConverter).map(RecipeBM::getPhotoDTOS, RecipeEntity::setPhotos))
                .addMappings(mpr -> mpr.using(ingredientConverter).map(RecipeBM::getIngredientDTOS, RecipeEntity::setIngredients))
                .addMappings(mpr -> mpr.map(RecipeBM::getPreparationHours, RecipeEntity::addPreparationHours))
                .addMappings(mpr -> mpr.map(RecipeBM::getPreparationMinutes, RecipeEntity::addPreparationMinutes))
                .addMappings(mpr -> mpr.map(RecipeBM::getCookingHours, RecipeEntity::addCookingHours))
                .addMappings(mpr -> mpr.map(RecipeBM::getCookingMinutes, RecipeEntity::addCookingMinutes));
//                .addMappings(mpr -> mpr.skip(RecipeBM::getAuthor, RecipeEntity::setAuthor));

        return recipeMapper;
    }

}
