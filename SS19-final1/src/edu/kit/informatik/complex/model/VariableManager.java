package edu.kit.informatik.complex.model;

import java.util.HashMap;
import java.util.Map;

public class VariableManager {

    private Map<String, ComplexNumber> variables = new HashMap<>();

    public void setVariable(String variable, ComplexNumber complexNumber) {
        variables.put(variable, complexNumber);
    }

    public ComplexNumber getVariable(String variable) {
        return variables.get(variable);
    }

    public boolean hasVariable(String variable) {
        return variables.containsKey(variable);
    }
}
