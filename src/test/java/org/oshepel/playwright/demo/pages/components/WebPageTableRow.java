package org.oshepel.playwright.demo.pages.components;

public interface WebPageTableRow {

   /* @SneakyThrows
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        List<Field> fields = Arrays.asList(getClass().getDeclaredFields());
        String delimiter = "|";

        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            Object obj = fields.get(i).get(this);
            result.append(Optional.ofNullable(obj).map(String::valueOf).orElse(emptyField()));
            result.append(delimiter);
            if (i == fields.size() - 2) {
                delimiter = "";
            }
        }
        return result.toString().replace("\n", "");
    }

    String emptyField() {
        return "";
    }*/

}
