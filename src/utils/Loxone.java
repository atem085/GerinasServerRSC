package utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Loxone
{
  public static String loxoneHost = "http://192.168.178.40/jdev/sps/";
  private static String loxoneAdmin = "admin";
  private static String loxonePassword = "Trappa2020!";

  private static void setConnectionHeaders(HttpURLConnection connection)
  {
    String auth = loxoneAdmin + ":" + loxonePassword;
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
    String authHeaderValue = "Basic " + new String(encodedAuth);
    connection.setRequestProperty("Authorization", authHeaderValue);
    connection.setRequestProperty("accept", "application/json");
  }

  public static String createUser(String userName) throws Exception
  {
    URL url = new URL(loxoneHost + "createuser/" + userName);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    setConnectionHeaders(connection);
    InputStream responseStream = connection.getInputStream();
    ObjectMapper mapper = new ObjectMapper();
    ANSWER answer = mapper.readValue(responseStream, ANSWER.class);
    return answer.result.value;
  }

  public static void deleteUser(String userID) throws Exception
  {
    URL url = new URL(loxoneHost + "deleteuser/" + userID);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    setConnectionHeaders(connection);
    InputStream responseStream = connection.getInputStream();
    ObjectMapper mapper = new ObjectMapper();
    ANSWER answer = mapper.readValue(responseStream, ANSWER.class);
    if (!answer.result.code.equalsIgnoreCase("200"))
      throw new Exception("Cannot remove user - code " + answer.result.code);
  }

  private static class ANSWER
  {
    public final Result result;

    public ANSWER(@JsonProperty("LL") Result result)
    {
      this.result = result;
    }
  }

  private static class Result
  {
    public final String control;
    public final String value;
    public final String code;

    public Result(@JsonProperty("control") String control,
                  @JsonProperty("value") String value,
                  @JsonProperty("Code") String code)
    {
      this.control = control;
      this.value = value;
      this.code = code;
    }
  }
}
