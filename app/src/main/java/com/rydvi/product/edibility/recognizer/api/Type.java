package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;

/**
 * Предназначен для страницы детализации гайда и страницы детализации продукта.
 * Не знаю как описать, что это такое. Это, блин, тип.
 */
public abstract class Type {

    /**
     * Имя должно быть равно названию каталога, где лежат html файлы
     * @return имя каталога и имя типа
     */
    public abstract String getName();

    /**
     * Перевод имени на понятный язык для пользователя
     * @param context - контекст для получения resources
     * @return переведенное имя
     */
    public abstract String getTranlatedName(Context context);

    /**
     *
     * @return Путь до html файла для webview
     */
    public abstract String getHtmlPath();
}
