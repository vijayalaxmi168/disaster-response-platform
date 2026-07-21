package com.disaster.rescue.dto;

import com.disaster.rescue.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusDto {
    @NotNull(message = "Status is required")
    private RequestStatus status;
}
