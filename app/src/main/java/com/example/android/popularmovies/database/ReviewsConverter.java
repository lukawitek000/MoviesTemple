package com.example.android.popularmovies.database;


import androidx.room.TypeConverter;

import com.example.android.popularmovies.models.Review;

class ReviewsConverter {


    private static final String NEW_REVIEW_REGEX = "\n\n\nnew review\n\n\n";
    private static final String CONTENT_REGEX = "\t\t\tcontent\t\t\t";


    @TypeConverter
    public static Review[] toReview(String reviewsString){
        if(reviewsString.equals("") || reviewsString.isEmpty()){
            return new Review[0];
        }
        String[] reviewInString = reviewsString.split(NEW_REVIEW_REGEX);
        Review[] reviews = new Review[reviewInString.length];
        for(int i = 0; i < reviewInString.length ; i++) {
            String[] rev = reviewInString[i].split(CONTENT_REGEX);
            Review review = new Review(rev[0], rev[1]);
            reviews[i] = review;
        }
        return reviews;
    }
    @TypeConverter
    public static String toString(Review[] reviews){
        StringBuilder builder = new StringBuilder();
        for(Review review : reviews){
            builder.append(review.getAuthor());
            builder.append(CONTENT_REGEX);
            builder.append(review.getContent());
            builder.append(NEW_REVIEW_REGEX);
        }
        return builder.toString();
    }


}
