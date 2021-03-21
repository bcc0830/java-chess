package chess.domain.position;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Vertical {
    ONE("1", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8);

    private final String symbol;
    private final int value;

    Vertical(final String symbol, final int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public static Vertical of(final String symbol) {
        return Arrays.stream(values())
            .filter(vertical -> vertical.symbol.equals(symbol))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 세로 위치를 찾을 수 없습니다."));
    }

    public static Vertical of(final int value) {
        return Arrays.stream(values())
            .filter(vertical -> vertical.value == value)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 세로 위치를 찾을 수 없습니다."));
    }

    public static List<String> verticalSymbols() {
        return Arrays.stream(values())
            .map(Vertical::getSymbol)
            .collect(Collectors.toList());
    }

    public String getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }

    public boolean isSameValue(final Vertical vertical) {
        return value == vertical.getValue();
    }
}
