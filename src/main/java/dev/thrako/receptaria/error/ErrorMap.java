package dev.thrako.receptaria.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMap {

    private final Map<String, List<String>> errors;

    public ErrorMap () {

        this.errors = new HashMap<>();
    }

    public void addError (String field, String message) {

        this.errors.putIfAbsent(field, new ArrayList<>());
        this.errors.get(field).add(message);

    }


}
