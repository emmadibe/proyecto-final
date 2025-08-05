package com.torneo.api.dto;

import com.torneo.api.models.TeamEntity;
import com.torneo.api.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamXPlayerRequestDTO
{
    private Long teamID;

    private Long userID;

    private boolean isCaptain;

}
