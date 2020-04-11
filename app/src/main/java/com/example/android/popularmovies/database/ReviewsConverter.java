package com.example.android.popularmovies.database;


import android.util.Log;

import androidx.room.TypeConverter;

import com.example.android.popularmovies.Review;

public class ReviewsConverter {

    @TypeConverter
    public static Review[] toReview(String reviewsString){
        if(reviewsString.equals("") || reviewsString.isEmpty()){
            return new Review[0];
        }
        String[] reviewInString = reviewsString.split("\n\n\nnew review\n\n\n");
        Review[] reviews = new Review[reviewInString.length];
        for(int i = 0; i < reviewInString.length ; i++) {
            String[] rev = reviewInString[i].split("\t\t\tcontent\t\t\t");
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
            builder.append("\t\t\tcontent\t\t\t");
            builder.append(review.getContent());
            builder.append("\n\n\nnew review\n\n\n");
        }
        //Log.i(ReviewsConverter.class.getName(), builder.toString());
        return builder.toString();
    }


}
