package com.example.practicalfive;

/** Validates input independently of Android so both Activities share the same rules. */
public class TextProcessor {
    public String processMunicipality(String municipality) {
        if (municipality == null) {
            return null;
        }
        String input = municipality.trim();
        if (input.isEmpty()) {
            return null;
        }
        String[] supportedMunicipalities = {"Beijing", "Shanghai", "Chongqing", "Tianjin"};
        for (String supported : supportedMunicipalities) {
            if (supported.equalsIgnoreCase(input)) {
                return supported;
            }
        }
        return null;
    }
}
