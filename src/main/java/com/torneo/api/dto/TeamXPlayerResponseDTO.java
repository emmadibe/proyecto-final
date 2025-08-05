package com.torneo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamXPlayerResponseDTO
{
    private Long id;
    private Long teamID;
    private Long userID;
    private boolean isCaptain;
}
