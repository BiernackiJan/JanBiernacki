package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CategoryUtilityTest {

    @Test
    void getCategoriesReturnsFullCategoriesSet() {
        ArrayList<String> categories = CategoryUtility.getCategories();
        assertEquals(5, categories.size());
        assertTrue(categories.contains("Home"));
        assertTrue(categories.contains("Work"));
        assertTrue(categories.contains("Hobby"));
        assertTrue(categories.contains("Holiday"));
        assertTrue(categories.contains("College"));
        assertFalse(categories.contains(""));
    }

    @Test
    void isValidCategoryTrueWhenCategoryExists() {
        assertTrue(CategoryUtility.isValidCategory("Home"));
        assertTrue(CategoryUtility.isValidCategory("home"));
        assertTrue(CategoryUtility.isValidCategory("WORK"));
    }

    @Test
    void isValidCategoryFalseWhenCategoryDoesNotExist() {
        assertFalse(CategoryUtility.isValidCategory("Hom"));
        assertFalse(CategoryUtility.isValidCategory("workk"));
        assertFalse(CategoryUtility.isValidCategory(""));
    }
}