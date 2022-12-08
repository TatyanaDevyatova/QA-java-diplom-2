//package userTests;
//
//import clients.UserClient;
//import dtos.UserDto;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.apache.http.HttpStatus.SC_CONFLICT;
//import static org.apache.http.HttpStatus.SC_OK;
//
//public class ChangeUserDataTests {
//
//    private UserClient userClient;
//    private UserDto userDto;
//    private String accessToken;
//
//    int successCreateStatusCode = SC_OK;
//    int failedCreateStatusCode = SC_CONFLICT;
//    boolean successCreateBody = true;
//    String failedCreateMessage = "Этот логин уже используется. Попробуйте другой.";
//
//    @Before
//    public void setUp() {
//        userClient = new UserClient();
//    }
//
//    @After
//    public void cleanUp() {
//        userClient.delete(accessToken);
//    }
//
////    @Test
////    public void changeUserDataWithAuthorizationReturnsOk() {
////    }
////
////    @Test
////    public void changeUserDataWithoutAuthorizationReturnsError() {
////    }
//}
//
