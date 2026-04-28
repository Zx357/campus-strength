package com.campuswall.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一前后端时间格式，兼容后台表单常用的 yyyy-MM-dd HH:mm:ss。
 */
@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
            builder.deserializerByType(LocalDateTime.class, new FlexibleLocalDateTimeDeserializer());
        };
    }

    private static class FlexibleLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        private final LocalDateTimeDeserializer standardDeserializer = LocalDateTimeDeserializer.INSTANCE;

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String text = parser.getText();
            if (text == null || text.isBlank()) {
                return null;
            }
            String value = text.trim();
            if (value.contains("T")) {
                return standardDeserializer.deserialize(parser, context);
            }
            return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
        }
    }
}
