package ru.insagent.management;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;
import ru.insagent.model.City;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestEntityManager
public abstract class ControllerIT {
    @LocalServerPort
    private int localServerPort;

    private String sessionCookie;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private TestEntityManager entityManager;

    private TransactionTemplate transactionTemplate;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private final static Pattern pattern = Pattern.compile("name=\"_csrf\" (value|content)=\"([^\"]+)\"/>");

    @Before
    public void prepare() {
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }

    @Before
    public void authorize() {
        LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", "vkulakov");
        form.add("password", "123456");

        HttpEntity<?> request = new HttpEntity<>(form, getHeaders());
        ResponseEntity<String> response = template.exchange("/login", HttpMethod.POST, request, String.class);
        sessionCookie = response.getHeaders().getFirst("Set-Cookie");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("http://localhost:" + localServerPort + "/");
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers;

        headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("/", HttpMethod.GET, request, String.class);
        if (sessionCookie == null) {
            sessionCookie = response.getHeaders().getFirst("Set-Cookie");
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Matcher matcher = pattern.matcher(response.getBody());
        assertThat(matcher.find()).isTrue();

        headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);
        headers.add("X-CSRF-TOKEN", matcher.group(2));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return headers;
    }

    protected String executeWithBody(String url, LinkedMultiValueMap<String, String> form, HttpMethod method) {
        HttpEntity<?> request = new HttpEntity<>(form, getHeaders());
        ResponseEntity<String> response = template.exchange(url, method, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        return response.getBody();
    }

    protected void executeWithoutBody(String url, LinkedMultiValueMap<String, String> form, HttpMethod method) {
        HttpEntity<?> request = new HttpEntity<>(form, getHeaders());
        ResponseEntity<String> response = template.exchange(url, method, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    protected <T> T getEntity(Class<T> clazz, Object key) {
        return transactionTemplate.execute(status -> entityManager.find(clazz, key));
    }

    protected <T> void removeEntity(Class<T> clazz, Object key) {
        transactionTemplate.execute(status -> {
            T entity = entityManager.find(clazz, key);
            if (entity != null) {
                entityManager.remove(entity);
            }
            return null;
        });
    }

    protected void createEntity(Object entity) {
        transactionTemplate.execute(status -> entityManager.persist(entity));
    }
}
