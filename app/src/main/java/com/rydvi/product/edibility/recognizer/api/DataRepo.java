package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataRepo {
    private static final DataRepo ourInstance = new DataRepo();

    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public final List<Product> loadProducts() {
        List<Product> products = null;
        String json = loadJson(Localization.getJsonPathByLanguage());
        if (json != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                products = objectMapper.readValue(json, new TypeReference<List<Product>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return products;
    }


    public String loadJson(String path) {
        String json = null;
        if(context==null){
            return null;
        }
        try {
            InputStream inputStream = context.getAssets().open(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public static DataRepo getInstance() {
        return ourInstance;
    }

    private DataRepo() {
    }
}
