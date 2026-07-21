package com.disaster.volunteer.client;

import com.disaster.volunteer.dto.AssignVolunteerRequestDto;
import com.disaster.volunteer.dto.RescueRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "RESCUE-REQUEST-SERVICE")
public interface RescueRequestClient {

    @GetMapping("/api/rescue-requests/{id}")
    RescueRequestDto getById(@PathVariable("id") Long id);

    @PutMapping("/api/rescue-requests/{id}/assign-volunteer")
    RescueRequestDto assignVolunteer(@PathVariable("id") Long id, @RequestBody AssignVolunteerRequestDto dto);
}
