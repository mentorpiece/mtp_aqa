package com.mtp.aqa.wrkshp.api.arch;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "of")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AirportDto {
    private long airportId;
    private long id;
    private String airportCode;
    private String airportName;
    private String city;
    private String state;
    private String country;

    @Builder.Default
    private List<Long> flightIds = new ArrayList<>();
}
