package com.rydvi.product.edibility.recognizer.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Попытка реализации enum класса в собственном исполнении
 * Необходим для повторяемых неизменяемых (статичных) GuideType и ProductType
 */
public abstract class TypeContainer {
    public final List<Type> listTypes = new ArrayList<>();
    protected final String PAGES_PATH = "file:///android_asset/pages/";
    public final String CONTAINER_PATH = PAGES_PATH + getContainerName() + "/";

    /**
     * Наименование контейнера (каталога), где лежат каталоги с типами
     * @return имя контейнера (каталога)
     */
    public abstract String getContainerName();

    /**
     * Ищет тим по имени
     *
     * @param name - имя типа
     * @return - сам тип
     */
    public Type findTypeByName(String name) {
        for (Type guideType : values()) {
            if (name.equalsIgnoreCase(guideType.getName())) {
                return guideType;
            }
        }
        return null;
    }

    /**
     * Путь до каталога типа с его именем, где лежат html
     * @param type
     * @return
     */
    public String getPathToType(Type type) {
        return CONTAINER_PATH + type.getName() + "/";
    }

    /**
     * Получение пути до html файла с переводом
     * @param type - тип для которого получаем путь
     * @return путь до html
     */
    public String getTranslateHtmlToType(Type type) {
        if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("ru")) {
            return getPathToType(type) + type.getName()+"_ru.html";
        } else {
            return getPathToType(type) + type.getName()+"_en.html";
        }
    }

    public abstract List<Type> values();
}