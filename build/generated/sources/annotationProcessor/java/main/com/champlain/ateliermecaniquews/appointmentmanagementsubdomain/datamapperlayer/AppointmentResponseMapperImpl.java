package com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datamapperlayer;

import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datalayer.Appointment;
import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.presentationlayer.AppointmentResponseModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-14T23:55:37-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class AppointmentResponseMapperImpl implements AppointmentResponseMapper {

    @Override
    public AppointmentResponseModel entityToResponseModel(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentResponseModel.AppointmentResponseModelBuilder appointmentResponseModel = AppointmentResponseModel.builder();

        appointmentResponseModel.customerId( appointment.getCustomerId() );
        appointmentResponseModel.vehicleId( appointment.getVehicleId() );
        appointmentResponseModel.status( appointment.getStatus() );
        appointmentResponseModel.appointmentDate( appointment.getAppointmentDate() );
        appointmentResponseModel.services( appointment.getServices() );
        appointmentResponseModel.comments( appointment.getComments() );

        appointmentResponseModel.appointmentId( appointment.getAppointmentIdentifier().getAppointmentId() );

        return appointmentResponseModel.build();
    }

    @Override
    public List<AppointmentResponseModel> entityToResponseModelList(List<Appointment> appointments) {
        if ( appointments == null ) {
            return null;
        }

        List<AppointmentResponseModel> list = new ArrayList<AppointmentResponseModel>( appointments.size() );
        for ( Appointment appointment : appointments ) {
            list.add( entityToResponseModel( appointment ) );
        }

        return list;
    }
}
