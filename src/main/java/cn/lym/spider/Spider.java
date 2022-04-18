package cn.lym.spider;

import cn.lym.spider.douban.MovieCrawler;

import java.io.IOException;

public class Spider {
    public void start() throws IOException {
        Crawler crawler = new MovieCrawler();
        crawler.run();
    }
}
