package com.example.android.popularmovies.database

import androidx.room.TypeConverter
import com.example.android.popularmovies.models.Review

class ReviewsConverter {
    private val NEW_REVIEW_REGEX = "\n\n\nnew review\n\n\n"
    private val CONTENT_REGEX = "\t\t\tcontent\t\t\t"

    @TypeConverter
    fun toReview(reviewsString: String): Array<Review?> {
        if (reviewsString == "" || reviewsString.isEmpty()) {
            return arrayOfNulls(0)
        }
        val reviewInString = reviewsString.split(NEW_REVIEW_REGEX).toTypedArray()
        val reviews = arrayOfNulls<Review>(reviewInString.size)
        for (i in reviewInString.indices) {
            val rev = reviewInString[i].split(CONTENT_REGEX).toTypedArray()
            val review = Review(rev[0], rev[1], "", "")
            reviews[i] = review
        }
        return reviews
    }

    @TypeConverter
    fun toString(reviews: Array<Review>): String {
        val builder = StringBuilder()
        for ((author, content) in reviews) {
            builder.append(author)
            builder.append(CONTENT_REGEX)
            builder.append(content)
            builder.append(NEW_REVIEW_REGEX)
        }
        return builder.toString()
    }
}