package com.mtp.aqa.wrkshp.api.arch;

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
