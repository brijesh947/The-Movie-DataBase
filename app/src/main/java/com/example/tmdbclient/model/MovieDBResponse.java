
package com.example.tmdbclient.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDBResponse implements Parcelable
{

    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> results = null;
    @SerializedName("total_results")
    @Expose
    private Integer total_results;
    @SerializedName("page")
    @Expose
    private Integer page;
    public final static Parcelable.Creator<MovieDBResponse> CREATOR = new Creator<MovieDBResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MovieDBResponse createFromParcel(Parcel in) {
            return new MovieDBResponse(in);
        }

        public MovieDBResponse[] newArray(int size) {
            return (new MovieDBResponse[size]);
        }

    }
    ;

    protected MovieDBResponse(Parcel in) {
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results , (Movie.class.getClassLoader()));
        this.total_results = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public MovieDBResponse() {
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results ;
    }

    public void setResults(List<Movie> results) {
        this.results  = results;
    }

    public Integer getTotalResults() {
        return total_results;
    }

    public void setTotalResults(Integer totalResults) {
        this.total_results = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(totalPages);
        dest.writeList(results );
        dest.writeValue(total_results);
        dest.writeValue(page);
    }

    public int describeContents() {
        return  0;
    }

}
