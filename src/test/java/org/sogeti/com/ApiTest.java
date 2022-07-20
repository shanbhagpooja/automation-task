package org.sogeti.com;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class ApiTest {

    @Test
    public void getPostalData_apiTest() {

        Response response = given().get("http://api.zippopotam.us/de/bw/stuttgart").thenReturn();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTimeIn(TimeUnit.SECONDS) <= 1);

        JsonPath data = response.jsonPath();
        Assert.assertEquals(data.getString("country"), "Germany");
        Assert.assertEquals(data.getString("state"), "Baden-Württemberg");

        Optional<Object> actualPlace = data.getList("places").stream().filter(place -> (place.toString().contains("70597") && place.toString().contains("Stuttgart Degerloch"))).findAny();
        Assert.assertTrue(actualPlace.isPresent());

    }
    @DataProvider(name = "places")
    public static Object[][] placesData() {
        return new Object[][]{
                {"us", "90210", "Beverly Hills"},
                {"us", "12345", "Schenectady"},
                {"ca", "B2R", "Waverley"}};
    }
    @Test(dataProvider = "places")
    public void getPostalData_by_country_and_code_apiTest(String country, String postCode, String placeName) {

        Response response = given().get("http://api.zippopotam.us/" + country + "/" + postCode).thenReturn();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTimeIn(TimeUnit.SECONDS) <= 1);

        JsonPath data = response.jsonPath();
        Assert.assertNotNull(data.getString("country"));

        List<JsonPath> places = data.getList("places");
        Assert.assertTrue(places.size() > 0);
        Optional<Object> actualPlace = data.getList("places").stream().filter(place -> (place.toString().contains(placeName))).findAny();
        Assert.assertTrue(actualPlace.isPresent());
    }
}
