package utils;
import java.util.ArrayList;
import java.util.Locale;

public class CategoryUtility {

        private static ArrayList<String> categories = new ArrayList<String>() {
            {
                add("Home");
                add("Work");
                add("Hobby");
                add("Holiday");
                add("College");

                System.out.println("ArrayList : " + categories);
            }
        };

        public static ArrayList <String> getCategories(){
            return categories;
        }

        public static boolean isValidCategory(String checkString) {
            for (int i = 0; i < categories.size(); i++){
                if (categories.get(i).toLowerCase(Locale.ROOT).equals(checkString.toLowerCase(Locale.ROOT))){
                    return true;
                }
            }
            return false;
        }


}
