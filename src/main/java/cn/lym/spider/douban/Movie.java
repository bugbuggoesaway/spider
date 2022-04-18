package cn.lym.spider.douban;

import cn.lym.spider.Bean;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Movie implements Bean {
    private int id;
    private int movieID;
    private String name;
    private String chineseName;
    private String otherNames;
    private String coverUrl;

    private int year;
    private String country;
    private String tags;

    private String director;
    private String actors;

    private String summary;
    private int rating;
    private int ratingCount;
}
