package com.champlain.ateliermecaniquews.reviewssubdomain.datamapperlayer;

import com.champlain.ateliermecaniquews.reviewssubdomain.datalayer.Review;
import com.champlain.ateliermecaniquews.reviewssubdomain.presentationlayer.ReviewResponseModel;
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
public class ReviewResponseMapperImpl implements ReviewResponseMapper {

    @Override
    public ReviewResponseModel entityToResponseModel(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponseModel.ReviewResponseModelBuilder reviewResponseModel = ReviewResponseModel.builder();

        reviewResponseModel.customerId( review.getCustomerId() );
        reviewResponseModel.appointmentId( review.getAppointmentId() );
        reviewResponseModel.comment( review.getComment() );
        reviewResponseModel.rating( review.getRating() );
        reviewResponseModel.reviewDate( review.getReviewDate() );
        reviewResponseModel.mechanicReply( review.getMechanicReply() );

        reviewResponseModel.reviewId( review.getReviewIdentifier().getReviewId() );

        return reviewResponseModel.build();
    }

    @Override
    public List<ReviewResponseModel> entityToResponseModelList(List<Review> reviews) {
        if ( reviews == null ) {
            return null;
        }

        List<ReviewResponseModel> list = new ArrayList<ReviewResponseModel>( reviews.size() );
        for ( Review review : reviews ) {
            list.add( entityToResponseModel( review ) );
        }

        return list;
    }
}
