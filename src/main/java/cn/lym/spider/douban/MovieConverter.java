package cn.lym.spider.douban;

import cn.lym.spider.Converter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MovieConverter implements Converter<Movie> {
    private Pattern movieUrlPattern = Pattern.compile("https://movie.douban.com/subject/(\\d+)/");
    private Pattern ratingCountPattern = Pattern.compile("(\\d+)人评价");
    private Pattern directorAndActorsPattern = Pattern.compile("导演: (.+) 主演: (.+)");

    @Override
    public Movie convert(Element element) {
        Movie.MovieBuilder builder = new Movie.MovieBuilder();
        //pic
        Element pic = element.selectFirst(".pic > a");
        builder.movieID(movieID(pic.attr("href")))
                .coverUrl(pic.selectFirst("img").attr("src"));

        //titles
        Elements titles = element.select(".info > div.hd > a > span");
        builder.name(unEscapeHTML(titles.get(1).text()))
                .chineseName(unEscapeHTML(titles.get(0).text()));
        if (titles.size() > 2) {
            builder.otherNames(unEscapeHTML(titles.get(2).text()));
        }

        //bd
        Element bd = element.selectFirst("div.info > div.bd");
        builder.rating(rating(bd.selectFirst("div.star > span.rating_num").text()))
                .ratingCount(ratingCount(bd.select("div.star > span").last().text()))
                .summary(bd.selectFirst("p.quote > span.inq").text());

        List<Node> nodes = bd.selectFirst("p").childNodes();
        if (nodes.size() >= 3) {
            Node directorAndActorsNode = nodes.get(0);
            if (directorAndActorsNode instanceof TextNode) {
                Matcher matcher = directorAndActorsPattern.matcher(unEscapeHTML(((TextNode) directorAndActorsNode).text()));
                if (matcher.matches()) {
                    builder.director(matcher.group(1).trim())
                            .actors(matcher.group(2).trim());
                }
            }

            Node yearCountryAndTagsNode = nodes.get(2);
            if (yearCountryAndTagsNode instanceof TextNode) {
                String[] yearCountryAndTags = unEscapeHTML(((TextNode) yearCountryAndTagsNode).text()).split("/");
                if (yearCountryAndTags.length >= 1) {
                    builder.year(year(yearCountryAndTags[0]));
                }
                if (yearCountryAndTags.length >= 2) {
                    builder.country(yearCountryAndTags[1].trim());
                }
                if (yearCountryAndTags.length >= 3) {
                    builder.tags(yearCountryAndTags[2].trim());
                }
            }
        }
        return builder.build();
    }

    private int movieID(String movieUrl) {
        Matcher matcher = this.movieUrlPattern.matcher(movieUrl);
        if (matcher.matches()) {
            String movieID = matcher.group(1);
            try {
                return Integer.parseInt(movieID);
            } catch (Exception e) {
                log.error("url: {}, movieID: {}, exception: {}", movieUrl, movieID, e);
            }
        }
        return 0;
    }

    private int rating(String text) {
        return new BigDecimal(text.trim()).multiply(new BigDecimal(10)).intValue();
    }

    private int ratingCount(String text) {
        Matcher matcher = ratingCountPattern.matcher(text);
        if (matcher.matches()) {
            String ratingCount = matcher.group(1);
            try {
                return Integer.parseInt(ratingCount);
            } catch (Exception e) {
                log.error("text: {}, ratingCount: {}, exception: {}", text, ratingCount, e);
            }
        }
        return 0;
    }

    private int year(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            log.error("text: {}, exception: {}", text, e);
        }
        return 0;
    }

    private String unEscapeHTML(String source) {
        String str = source.replaceAll("&nbsp;", " ").trim();
        if (str.startsWith("/")) {
            str = str.substring(1).trim();
        }
        return str;
    }
}
