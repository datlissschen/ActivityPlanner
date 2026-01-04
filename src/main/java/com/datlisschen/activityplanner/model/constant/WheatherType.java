package com.datlisschen.activityplanner.model.constant;

import lombok.Getter;

public enum WheatherType {
    SUNNY("Sunny", "☀️"),
    PARTLY_CLOUDY("Partly Cloudy", "⛅"),
    THUNDERSTORMS("Thunderstorms", "🌩️"),
    LIGHT_RAIN("Light Rain", "🌧️"),
    SNOWFLAKES("Snowflakes", "❄️"),
    WIND_GUSTS("Wind Gusts", "💨"),
    FOG("Fog", "🌫️"),
    OVERCAST("Overcast", "☁️");

    private final String displayName;
    private final String emoji;

    WheatherType(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }
}
