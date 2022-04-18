package cn.lym.spider.douban;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MovieConverterTest {
    @Test
    public void test() {
        Pattern directorAndActorsPattern = Pattern.compile("导演: (.+) 主演: (.+)");
        String str = "导演: 弗兰克·德拉邦特 Frank Darabont 主演: 蒂姆·罗宾斯 Tim Robbins /...";
        Matcher matcher = directorAndActorsPattern.matcher(str);
        if (matcher.matches()) {
            System.out.println("matches");
            System.out.println("director: " + matcher.group(1));
            System.out.println("actors: " + matcher.group(2));
        } else {
            System.out.println("not match");
        }
    }
}