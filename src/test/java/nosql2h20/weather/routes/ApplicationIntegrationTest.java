package nosql2h20.weather.routes;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class ApplicationIntegrationTest {

    private final String GREETING_TEMPLATE = "^Hello, %s!\\nBest wishes from node \\d+.";
    private final String DEFAULT_USER = "%username%";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultGreeting() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.content").value(
                                matchesPattern(String.format(GREETING_TEMPLATE, DEFAULT_USER))
                        )
                );
    }

    @Test
    public void shouldReturnTailoredGreeting() throws Exception {
        this.mockMvc.perform(get("/")
                .param("name", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.content").value(
                                matchesPattern(String.format(GREETING_TEMPLATE, "test"))
                        )
                );
    }
}
