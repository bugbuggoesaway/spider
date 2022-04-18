package cn.lym.spider.douban;

import cn.lym.spider.Converter;
import cn.lym.spider.Crawler;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MovieCrawler implements Crawler {
    private static final String SEED = "https://movie.douban.com/top250";
    private static final int MAX_PAGE = 10;
    private static final int PAGE_SIZE = 25;

    private String seed;
    private int currentPage;

    private Converter<Movie> converter = new MovieConverter();

    public MovieCrawler() {
        this(SEED);
    }

    public MovieCrawler(String seed) {
        this(seed, 1);
    }

    public MovieCrawler(String seed, int currentPage) {
        this.seed = seed;
        this.currentPage = currentPage;
    }

    @Override
    public void run() throws IOException {
        String url = this.seed + "?start=" + (this.currentPage - 1) * PAGE_SIZE;
        Document document = Jsoup.connect(url)
                .get();
        List<Movie> movies = document.select("#content ol.grid_view > li > div.item") //items
                .stream().
                map(this.converter::convert).
                collect(Collectors.toList());
        log.debug("size: {}", movies.size());
        movies.forEach(movie -> {
            System.out.println("movie: " + movie);
            log.debug("movie: {}", movie);
        });
    }

    @Override
    public boolean hasNext() {
        return this.currentPage <= MAX_PAGE;
    }

    @Override
    public Crawler next() {
        return new MovieCrawler(this.seed, this.currentPage + 1);
    }
}
