package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;

/**
 * Предназначен для страницы детализации гайда и страницы детализации продукта.
 * Не знаю как описать, что это такое. Это, блин, тип.
 */
public abstract class Type {
    public abstract String getName();

    public abstract String getTranlatedName(Context context);

    public abstract String getHtmlPath();
}
