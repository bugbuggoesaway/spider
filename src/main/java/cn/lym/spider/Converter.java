package cn.lym.spider;

import org.jsoup.nodes.Element;

public interface Converter<E extends Bean> {
    E convert(Element element);
}
