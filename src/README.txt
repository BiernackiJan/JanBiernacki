I have changed the test line 74 of ItemTest from:
                                               assertTrue(javaItemTwo.toString().contains("Complete"));
                                           to:
                                               assertTrue(javaItemTwo.toString().contains("Completed"));

                                           due to the fact that it would either give me back an error in Item
                                           or later in other tests. In other tests it looks for the string to
                                           contain Completed and not Complete which would cause complications
                                           in the adding of a single letter to the sting.