package edu.lehigh.cse216.team24.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class AppTest {
    private Javalin app;
    private final int PORT = 8081; // Using different port for testing
    private final String BASE_URL = "http://localhost:" + PORT;
    private final Gson gson = new Gson();
    private HttpClient client;
    private Routes routes;
    private Buzz buzzMock;
    private Context ctx;

    @Mock
    private DriveServiceValidator mockValidator;

    @Mock
    private Buzz mockBuzz;

    
    /** 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        buzzMock = mock(Buzz.class);
        routes = new Routes(buzzMock, gson);
        app = mock(Javalin.class);
        ctx = mock(Context.class);

        MockitoAnnotations.openMocks(this);
        client = HttpClient.newHttpClient();

        app = Javalin.create()
                .get("/gdrive", ctx -> {
                    ctx.status(200);
                    ctx.contentType("application/json");

                    String out;
                    if (mockValidator.validateSetup()) {
                        out = "Service account setup is valid!";
                        mockValidator.printServiceAccountEmail();
                    } else {
                        out = "Service account setup failed validation.";
                    }

                    StructuredResponse resp = new StructuredResponse("ok", null, null, null, out);
                    ctx.result(gson.toJson(resp));
                });

        app.start(PORT);
    }

    @AfterEach
    public void tearDown() {
        if (app != null) {
            app.stop();
        }
    }

    @Test
    /**
     * This function tests using the /gdrive route which validates the service
     * account, and access to google drive (for debugging and testing)
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void testSuccessfulValidation() throws IOException, InterruptedException {
        // Arrange
        when(mockValidator.validateSetup()).thenReturn(true);
        doNothing().when(mockValidator).printServiceAccountEmail();

        // Act
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/gdrive"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());

        StructuredResponse resp = gson.fromJson(response.body(), StructuredResponse.class);
        assertEquals("ok", resp.mStatus());
        assertEquals("Service account setup is valid!", resp.mData());

        // Verify mock interactions
        verify(mockValidator).validateSetup();
        verify(mockValidator).printServiceAccountEmail();
    }

    @Test
    /**
     * Test to make sure if credentials or service account isn't valid, the route
     * fails
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void testFailedValidation() throws IOException, InterruptedException {
        // Arrange
        when(mockValidator.validateSetup()).thenReturn(false);

        // Act
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/gdrive"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());

        StructuredResponse resp = gson.fromJson(response.body(), StructuredResponse.class);
        assertEquals("ok", resp.mStatus());
        assertEquals("Service account setup failed validation.", resp.mData());

        // Verify mock interactions
        verify(mockValidator).validateSetup();
        verify(mockValidator, never()).printServiceAccountEmail();
    }

}
