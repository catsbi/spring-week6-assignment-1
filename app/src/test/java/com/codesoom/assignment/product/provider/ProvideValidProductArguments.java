package com.codesoom.assignment.product.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ProvideValidProductArguments implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("츄르", "고양이 주식회사", 3000, "https://sc04.alicdn.com/kf/HTB10TD8ipmWBuNjSspdq6zugXXag.jpg"),
                Arguments.of("스크래쳐", "고양이 주식회사", 15000, "http://image.auction.co.kr/itemimage/18/90/d6/1890d6a786.jpg"),
                Arguments.of("펫모닝", "고양이 주식회사", 4500, "https://static19.fitpetmall.co.kr/modify-images/h/750/w/750/q/95/p/images/product/cover/e1/00/e100bab3513945cd993334b7ffa027a4.png"),
                Arguments.of("두더지잡기", "고양이 주식회사", 35000, "http://ohpackege.com/web/upload/NNEditor/20190113/1_shop1_170202.jpg")
        );
    }
}
