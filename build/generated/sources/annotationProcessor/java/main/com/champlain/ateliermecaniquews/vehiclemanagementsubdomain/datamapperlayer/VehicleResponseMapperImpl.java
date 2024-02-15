package com.champlain.ateliermecaniquews.vehiclemanagementsubdomain.datamapperlayer;

import com.champlain.ateliermecaniquews.vehiclemanagementsubdomain.datalayer.Vehicle;
import com.champlain.ateliermecaniquews.vehiclemanagementsubdomain.presentationlayer.VehicleResponseModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-15T02:19:40-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class VehicleResponseMapperImpl implements VehicleResponseMapper {

    @Override
    public VehicleResponseModel entityToResponseModel(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }

        VehicleResponseModel.VehicleResponseModelBuilder vehicleResponseModel = VehicleResponseModel.builder();

        vehicleResponseModel.userId( vehicle.getUserId() );
        vehicleResponseModel.transmission_type( vehicle.getTransmission_type() );
        vehicleResponseModel.make( vehicle.getMake() );
        vehicleResponseModel.model( vehicle.getModel() );
        vehicleResponseModel.year( vehicle.getYear() );
        vehicleResponseModel.mileage( vehicle.getMileage() );

        vehicleResponseModel.vehicleId( vehicle.getVehicleIdentifier().getVehicleId() );

        return vehicleResponseModel.build();
    }

    @Override
    public List<VehicleResponseModel> entityToResponseModelList(List<Vehicle> vehicles) {
        if ( vehicles == null ) {
            return null;
        }

        List<VehicleResponseModel> list = new ArrayList<VehicleResponseModel>( vehicles.size() );
        for ( Vehicle vehicle : vehicles ) {
            list.add( entityToResponseModel( vehicle ) );
        }

        return list;
    }
}
