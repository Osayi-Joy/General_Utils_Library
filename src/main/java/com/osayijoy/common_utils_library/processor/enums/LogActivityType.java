package com.osayijoy.common_utils_library.processor.enums;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public enum LogActivityType {

    LOGIN_ACTIVITY("Login Activity"),
    UPDATE_ACTIVITY("Update Activity"),
    UPDATE_REQUIRED_ACTIVITY("Update Required Activity"),
    UPDATE_TREATED_ACTIVITY("Update Treated Activity"),
    DELETE_ACTIVITY("Delete Activity"),
    DELETE_REQUIRED_ACTIVITY("Delete Required Activity"),
    DELETE_TREATED_ACTIVITY("Delete Treated Activity"),
    CREATE_ACTIVITY("Create Activity"),
    CREATE_REQUIRED_ACTIVITY("Create Required Activity"),
    CREATE_TREATED_ACTIVITY("Create Treated Activity"),
    LOGOUT_ACTIVITY("Logout Activity");

    private final String prettyName;

    LogActivityType(String prettyName){
        this.prettyName = prettyName;
    }

    @Override
    public String toString() {
        return  prettyName;
    }

    public static List<LogActivityTypeDTO> getAllLogActivityTypeDTO(){

        return  Arrays.stream(values()).map(x-> new LogActivityTypeDTO(x.prettyName, x)).toList();

        // return Arrays.asList(values());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogActivityTypeDTO{
        private String logActivityName;
        private LogActivityType logActivityType;

    }

}
