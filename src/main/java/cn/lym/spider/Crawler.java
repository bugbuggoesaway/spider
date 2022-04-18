package cn.lym.spider;

import java.io.IOException;

public interface Crawler {
    void run() throws IOException;

    boolean hasNext();

    Crawler next();
}
