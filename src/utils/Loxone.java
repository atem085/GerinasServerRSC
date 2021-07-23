package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
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

  private static InputStream getConnectionResponse(URL url) throws IOException
  {
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    setConnectionHeaders(connection);
    return connection.getInputStream();
  }

  public static String createUser(String userName) throws Exception
  {
    URL url = new URL(loxoneHost + "createuser/" + userName);
    InputStream responseStream = getConnectionResponse(url);
    ObjectMapper mapper = new ObjectMapper();
    SimpleAnswer simpleAnswer = mapper.readValue(responseStream, SimpleAnswer.class);
    return simpleAnswer.simpleResult.value;
  }

  public static void deleteUser(String userUUID) throws Exception
  {
    URL url = new URL(loxoneHost + "deleteuser/" + userUUID);
    InputStream responseStream = getConnectionResponse(url);
    ObjectMapper mapper = new ObjectMapper();
    SimpleAnswer simpleAnswer = mapper.readValue(responseStream, SimpleAnswer.class);
    if (!simpleAnswer.simpleResult.code.equalsIgnoreCase("200"))
      throw new Exception("Cannot remove user - error code " + simpleAnswer.simpleResult.code);
  }

  public static String getGroupUUID(String groupName) throws Exception
  {
    URL url = new URL(loxoneHost + "getgrouplist");
    InputStream responseStream = getConnectionResponse(url);
    ObjectMapper mapper = new ObjectMapper();
    SimpleAnswer simpleAnswer = mapper.readValue(responseStream, SimpleAnswer.class);
    List<Group> groups = mapper.readValue(simpleAnswer.simpleResult.value, new TypeReference<List<Group>>(){});
    for (int i = 0; i < groups.size(); i++)
    {
      groups.get(i).toString();
      if (groups.get(i).name.equalsIgnoreCase(groupName))
        return groups.get(i).uuid;
    }
    throw new Exception("Group not found!");
  }

  public static void setGroup(String userUUID, String groupName) throws Exception
  {
    URL url = new URL(loxoneHost + "assignusertogroup/" + userUUID + "/" + getGroupUUID(groupName));
    InputStream responseStream = getConnectionResponse(url);
    ObjectMapper mapper = new ObjectMapper();
    SimpleAnswer simpleAnswer = mapper.readValue(responseStream, SimpleAnswer.class);
    if (!simpleAnswer.simpleResult.code.equalsIgnoreCase("200"))
      throw new Exception("Cannot set group - error code " + simpleAnswer.simpleResult.code);
  }


  private static class SimpleAnswer
  {
    public final SimpleResult simpleResult;

    public SimpleAnswer(@JsonProperty("LL") SimpleResult simpleResult)
    {
      this.simpleResult = simpleResult;
    }
  }

  private static class SimpleResult
  {
    public final String control;
    public final String value;
    public final String code;

    public SimpleResult(@JsonProperty("control") String control,
                        @JsonProperty("value") String value,
                        @JsonProperty("Code") String code)
    {
      this.control = control;
      this.value = value;
      this.code = code;
    }
  }

  private static class Group
  {
    public final String name;
    public final String description;
    public final String uuid;
    public final String type;
    public final String userRights;

    public Group(@JsonProperty("name") String name,
                 @JsonProperty("description") String description,
                 @JsonProperty("uuid") String uuid,
                 @JsonProperty("type") String type,
                 @JsonProperty("userRights") String userRights)
    {
      this.name = name;
      this.description = description;
      this.uuid = uuid;
      this.type = type;
      this.userRights = userRights;
    }

    @Override
    public String toString() {
      return "Group{" +
          "name='" + name + '\'' +
          ", description='" + description + '\'' +
          ", uuid='" + uuid + '\'' +
          ", type=" + type +
          ", userRights=" + userRights +
          '}';
    }
  }
}
