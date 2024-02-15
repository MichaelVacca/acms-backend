package com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.presentationlayer;

import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datalayer.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class AppointmentRequestModel {

        private String customerId;
        private String vehicleId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime appointmentDate;
        private String services;
        private String comments;
        private Status status;
}
