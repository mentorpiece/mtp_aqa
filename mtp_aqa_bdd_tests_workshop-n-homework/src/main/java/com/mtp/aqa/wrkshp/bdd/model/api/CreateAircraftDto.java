package com.mtp.aqa.wrkshp.bdd.model.api;

import lombok.*;

@Data
@Builder(builderMethodName = "of")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateAircraftDto {
    private String manufacturer;
    private String model;
    private Integer numberOfSeats;
}
