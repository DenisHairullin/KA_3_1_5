package rest;

import rest.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class Application {
    public static final String URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        StringBuilder code = new StringBuilder();
        RestTemplate template = new RestTemplate();

        /* Getting JSESSIONID */
        String cookie = template.headForHeaders(URL).getValuesAsList(HttpHeaders.SET_COOKIE)
                .stream().filter(x -> x.startsWith("JSESSIONID")).findFirst().orElseThrow(
                        () -> new RuntimeException("JSESSIONID cookie not found")
                );
        System.out.println("cookie = " + cookie);

        /* Preparing headers */
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        /* Storing user */
        User user = new User(3L, "James", "Brown", (byte) 255);
        code.append(template.exchange(URL, HttpMethod.POST, new HttpEntity<>(user, headers), String.class).getBody());

        /* Updating user */
        user.setName("Thomas");
        user.setLastName("Shelby");
        code.append(template.exchange(URL, HttpMethod.PUT, new HttpEntity<>(user, headers), String.class).getBody());

        /* Deleting user */
        code.append(template.exchange(URL + "/3", HttpMethod.DELETE, new HttpEntity<>(null, headers),
                String.class).getBody());

        System.out.println("code = " + code);
    }
}
