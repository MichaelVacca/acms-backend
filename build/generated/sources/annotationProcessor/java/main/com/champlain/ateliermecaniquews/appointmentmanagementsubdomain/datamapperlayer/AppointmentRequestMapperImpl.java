package com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datamapperlayer;

import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datalayer.Appointment;
import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.presentationlayer.AppointmentRequestModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-15T02:19:40-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class AppointmentRequestMapperImpl implements AppointmentRequestMapper {

    @Override
    public Appointment requestModelToEntity(AppointmentRequestModel appointmentRequestModel) {
        if ( appointmentRequestModel == null ) {
            return null;
        }

        Appointment appointment = new Appointment();

        appointment.setCustomerId( appointmentRequestModel.getCustomerId() );
        appointment.setVehicleId( appointmentRequestModel.getVehicleId() );
        appointment.setAppointmentDate( appointmentRequestModel.getAppointmentDate() );
        appointment.setServices( appointmentRequestModel.getServices() );
        appointment.setComments( appointmentRequestModel.getComments() );
        appointment.setStatus( appointmentRequestModel.getStatus() );

        return appointment;
    }
}
