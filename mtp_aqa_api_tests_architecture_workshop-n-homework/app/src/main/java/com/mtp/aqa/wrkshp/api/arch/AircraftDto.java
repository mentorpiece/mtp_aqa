package com.mtp.aqa.wrkshp.api.arch;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "of")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AircraftDto {
    private long aircraftId;
    private long id;
    private String manufacturer;
    private String model;
    private Integer numberOfSeats;

    @Builder.Default
    private List<Long> flightIds = new ArrayList<>();
}
