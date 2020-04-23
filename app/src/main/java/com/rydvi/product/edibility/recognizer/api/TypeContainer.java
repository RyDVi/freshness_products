package com.rydvi.product.edibility.recognizer.api;

import java.util.List;

/**
 * Попытка реализации enum класса в собственном исполнении
 * Необходим для повторяемых неизменяемых (статичных) GuideType и ProductType
 */
public abstract class TypeContainer{
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

    public abstract List<Type> values();
}