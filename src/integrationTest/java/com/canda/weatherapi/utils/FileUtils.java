package com.canda.weatherapi.utils;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FileUtils {

    public static String readFileAsString(final String filePath) {

        try (final InputStream resStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            assertThat(resStream).isNotNull();
            return new String(resStream.readAllBytes());
        } catch (final IOException e) {
            throw new IllegalStateException("Error reading file at " + filePath, e);
        }
    }

}