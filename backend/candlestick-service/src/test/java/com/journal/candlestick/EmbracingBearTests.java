package com.journal.candlestick;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.models.PatternName;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;
import com.journal.candlestick.services.patterns.sell.EmbracingBear;

@SpringBootTest
public class EmbracingBearTests {
    private List<CandleStickDto> data = new ArrayList<>();
    private EmbracingBear pattern;

    @Autowired
    private CandlestickConfig config;

    @BeforeEach
    void setup() {
        pattern = new EmbracingBear(config);

        data.add(new CandleStickDto(1l, "", 0.0, 50.0, 0.0, 40.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(2l, "", 40.0, 80.0, 30.0, 80.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(3l, "", 80.0, 120.0, 80.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(4l, "", 100.0, 150.0, 90.0, 140.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(5l, "", 150.0, 200.0, 120.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
    }

    @Test
    void validPenetration() {
        data.add(new CandleStickDto(6l, "", 200.0, 300.0, 200.0, 280.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 300.0, 310.0, 180.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.SELL.equals(result.getSignal()));
        assertTrue(PatternName.EMBRACING_BEAR.equals(result.getName()));
        assertTrue(!result.isTrap());
    }

    @Test
    void tohighDecissionCandle() {
        data.add(new CandleStickDto(6l, "", 200.0, 300.0, 200.0, 280.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 300.0, 310.0, 200.0, 220.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result == null);
    }

    @Test
    void embracingBearWithOverlap() {
        data.add(new CandleStickDto(6l, "", 200.0, 700.0, 0.0, 280.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 200.0, 300.0, 200.0, 280.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(8l, "", 300.0, 310.0, 180.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.SELL.equals(result.getSignal()));
        assertTrue(PatternName.EMBRACING_BEAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.INSIDE_BAR.equals(result.getTrapType()));
    }

    @Test
    void sellAtBottom() {
        data.add(new CandleStickDto(6l, "", 50.0, 150.0, 50.0, 130.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 150.0, 160.0, 30.0, 30.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.EMBRACING_BEAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.SELL_AT_BOTTOM.equals(result.getTrapType()));
    }

    @Test
    void tooLongShadowTrap() {
        data.add(new CandleStickDto(6l, "", 250.0, 350.0, 250.0, 330.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 350.0, 360.0, 0.0, 230.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.SELL.equals(result.getSignal()));
        assertTrue(PatternName.EMBRACING_BEAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.TOO_LONG_SHADOWS.equals(result.getTrapType()));
    }
}
