package com.wastewise.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class PriorityResponse {
    private Long taskId;
    private Long areaId;
    private String areaName;
    private Integer priorityScore;
    private String recommendedAction;
    private String justification;
    private String status;
    private String assignedToName;
}
