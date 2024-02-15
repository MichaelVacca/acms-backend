package com.champlain.ateliermecaniquews.reviewssubdomain.datamapperlayer;

import com.champlain.ateliermecaniquews.reviewssubdomain.datalayer.Review;
import com.champlain.ateliermecaniquews.reviewssubdomain.presentationlayer.ReviewRequestModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-14T23:55:37-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class ReviewRequestMapperImpl implements ReviewRequestMapper {

    @Override
    public Review requestModelToEntity(ReviewRequestModel reviewRequestModel) {
        if ( reviewRequestModel == null ) {
            return null;
        }

        Review review = new Review();

        review.setCustomerId( reviewRequestModel.getCustomerId() );
        review.setAppointmentId( reviewRequestModel.getAppointmentId() );
        review.setComment( reviewRequestModel.getComment() );
        review.setRating( reviewRequestModel.getRating() );
        review.setReviewDate( reviewRequestModel.getReviewDate() );

        return review;
    }
}
