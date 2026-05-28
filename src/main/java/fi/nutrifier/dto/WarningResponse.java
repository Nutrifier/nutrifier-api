package fi.nutrifier.dto;

import fi.nutrifier.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class WarningResponse extends ApiResponse {
    private ResponseCode code;
    private String message;

    public static ArrayList<WarningResponse> of(ResponseCode code, String message) {
        return new ArrayList<>(Collections.singletonList(new WarningResponse(code, message)));
    }

    public static ArrayList<WarningResponse> of(Map<ResponseCode, String> map) {
        ArrayList<WarningResponse> list = new ArrayList<>();
        map.forEach((code, message) -> list.add(new WarningResponse(code, message)));
        return list;
    }
}