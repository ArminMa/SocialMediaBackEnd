package se.kth.awesome.controller.ping;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.kth.awesome.pojos.ping.PingPojo;
import se.kth.awesome.util.GsonX;
import se.kth.awesome.util.MediaTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PingControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc ;
    //	private MockRestServiceServer mockServer;
    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void ping1() throws Exception {
        String pingReturnd =
                this.mockMvc.perform(get("/test/ping1").accept(MediaTypes.textPlain))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        assertThat(pingReturnd).isNotNull();
        assertThat(pingReturnd.equals("Hello  World"));
    }

    @Test
    public void ping2() throws Exception {
        String textInserted = "stringValue";
        String valuReturnd =
                this.mockMvc.perform(get("/test/ping2?name="+ textInserted).accept(MediaTypes.textPlain))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();
        assertThat(valuReturnd).isNotNull();
        assertThat(valuReturnd).isEqualTo("Hello " + textInserted);
    }

    @Test
    public void ping7() throws Exception {
        System.out.println("----------------- PingControllerTest.ping7-start ----------------------------");
        PingPojo ping = new PingPojo("Ping Armin", "ignore me", "keyUserServer = my token should be here");
        PingPojo pingReturnd = GsonX.gson.fromJson(
                this.mockMvc.perform
                        (
                                post("/test/ping7/testName")
                                        .contentType(MediaTypes.JsonUtf8)
                                        .content(GsonX.gson.toJson(ping))
                                        .header("keyUserServer","my token should be here")
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andExpect(header().string("keyUserServer","some random token"))
                        .andExpect(header().string("info", "mor header info"))
                        .andReturn().getResponse().getContentAsString()
                , PingPojo.class);
        assertThat(pingReturnd).isNotNull();
        assertThat(pingReturnd.equals(ping));
        System.out.println("----------------- PingControllerTest.ping7-end ----------------------------");
    }
    //TODO writhe test for ping 3,4,5,6
    @Test
    public void ping8() throws Exception {
        PingPojo pingReturnd = GsonX.gson.fromJson(
                this.mockMvc.perform(get("/test/ping8/test@gmail.com").accept(MediaTypes.JsonUtf8))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andReturn().getResponse().getContentAsString()
                , PingPojo.class);
        assertThat(pingReturnd).isNotNull();
        PingPojo ping = new PingPojo(null, null, "test@gmail.com");
        assertThat(pingReturnd.equals(ping));
    }
}