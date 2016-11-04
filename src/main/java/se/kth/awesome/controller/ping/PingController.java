package se.kth.awesome.controller.ping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.awesome.pojos.ping.PingPojo;
import se.kth.awesome.util.GsonX;
import se.kth.awesome.util.MediaTypes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PingController {

    @RequestMapping(value = "/ping1", method = RequestMethod.GET)
    @ResponseBody
    public String ping1() {
        return "Hello  World";
    }

    @RequestMapping(value = "/ping2", method = RequestMethod.GET)
    public String ping2(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello " + name;
    }

    @RequestMapping(value = "/ping3",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET)
    public @ResponseBody PingPojo ping3(
            HttpServletRequest request,
            HttpServletResponse response) {

        PingPojo pingPojo = new PingPojo("Ping ping1!", "ignore me", "not Ignored");
        response.setStatus(HttpStatus.OK.value());
        return pingPojo;
    }

    @RequestMapping(value = "/ping4/{name}", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public
    @ResponseBody
    PingPojo ping4(@PathVariable("name") String name) {
        PingPojo pingPojo = new PingPojo("Ping " + name, "ignore me", "not Ignored");
        return pingPojo;
    }

    @RequestMapping(value = "/ping5",
            produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public @ResponseBody PingPojo ping5(@RequestParam("name") String name) {
        PingPojo pingPojo = new PingPojo("Ping " + name, "ignore me", "not Ignored");
        return pingPojo;
    }


    @RequestMapping(value = "/ping6/{name}",
                    method = RequestMethod.GET,
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody PingPojo ping6(
            @RequestHeader(name = "jwt", defaultValue = "where is the Token?") String jwt,
            @PathVariable("name") String name) {
        PingPojo pingPojo = new PingPojo("Ping " + name, "ignore me", "keyUserServer = " + jwt);
        return pingPojo;
    }

    @RequestMapping(
            value = "/ping7/{name}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.POST)
    public @ResponseBody PingPojo ping7(
            @RequestHeader(name = "jwt", defaultValue = "where is the Token?") String jwt,
            @PathVariable("name") String name,
            @RequestBody PingPojo pingPojo,
            HttpServletRequest request,
            HttpServletResponse response) {

        response.addHeader("keyUserServer", "some random token");
        response.addHeader("info", "mor header info");
        response.setStatus(HttpStatus.OK.value());
        return pingPojo;
    }

    @RequestMapping(
            value = "/ping8/{email:.+}",
            method = RequestMethod.GET)
    public ResponseEntity<?> ping8(@PathVariable("email") String userEmail) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaTypes.JsonUtf8)
                .body(new PingPojo(null,null,userEmail));

    }


    private Logger logger = LoggerFactory.getLogger(getClass());
    @ExceptionHandler public @ResponseBody
    ResponseEntity<?> handleDemoException(Exception exception,
                                          HttpServletRequest req) {
        logger.error("\nJoseException - " + exception.toString() + "\n");

        // Because we are handling the error, the server thinks everything is
        // OK, so the status is 200. So let's set it to something else.
        req.setAttribute("javax.servlet.error.status_code", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(MediaTypes.JsonUtf8)
                .body(GsonX.gson.toJson(exception.toString()));


    }

}
