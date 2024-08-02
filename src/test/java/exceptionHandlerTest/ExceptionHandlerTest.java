package exceptionHandlerTest;

import com.example.capstone_project.config.GlobalExceptionHandler;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {

    }


    @Test
    public void testParseMessageToListExceptionResponse_PrivateMethod_MultipleFields() throws Exception {
        // Sử dụng reflection để lấy phương thức private
        Method parseMethod = GlobalExceptionHandler.class.getDeclaredMethod("parseMessageToListExceptionResponse", String.class);
        parseMethod.setAccessible(true);

        // Dữ liệu đầu vào
        String errorMessage = "createUser.userBody.phoneNumber: Phone number must be between 10 and 15 digits, " +
                "createUser.userBody.address: Address cannot be empty, " +
                "createUser.userBody.email: Email is invalid, " +
                "createUser.userBody.fullName: Fullname cannot be empty, " +
                "createUser.userBody.fullName: Full name must contain only letters and spaces";

        // Gọi phương thức private thông qua reflection
        @SuppressWarnings("unchecked")
        List<ExceptionResponse> result = (List<ExceptionResponse>) parseMethod.invoke(globalExceptionHandler, errorMessage);

        // Kết quả mong đợi
        ExceptionResponse expectedResponse1 = new ExceptionResponse("phoneNumber", "Phone number must be between 10 and 15 digits");
        ExceptionResponse expectedResponse2 = new ExceptionResponse("address", "Address cannot be empty");
        ExceptionResponse expectedResponse3 = new ExceptionResponse("email", "Email is invalid");
        ExceptionResponse expectedResponse4 = new ExceptionResponse("fullName", "Fullname cannot be empty");
        ExceptionResponse expectedResponse5 = new ExceptionResponse("fullName", "Full name must contain only letters and spaces");

        // Kiểm tra kết quả
        assertEquals(5, result.size());
        assertEquals(expectedResponse1.getField(), result.get(0).getField());
        assertEquals(expectedResponse1.getMessage(), result.get(0).getMessage());
        assertEquals(expectedResponse2.getField(), result.get(1).getField());
        assertEquals(expectedResponse2.getMessage(), result.get(1).getMessage());
        assertEquals(expectedResponse3.getField(), result.get(2).getField());
        assertEquals(expectedResponse3.getMessage(), result.get(2).getMessage());
        assertEquals(expectedResponse4.getField(), result.get(3).getField());
        assertEquals(expectedResponse4.getMessage(), result.get(3).getMessage());
        assertEquals(expectedResponse5.getField(), result.get(4).getField());
        assertEquals(expectedResponse5.getMessage(), result.get(4).getMessage());
    }


    @Test
    void testParseMessageToListExceptionResponse_PrivateMethod_ComplexMessages() throws Exception {
        // Sử dụng reflection để lấy phương thức private
        Method parseMethod = GlobalExceptionHandler.class.getDeclaredMethod("parseMessageToListExceptionResponse", String.class);
        parseMethod.setAccessible(true);

        String errorString = "changePassword.changePasswordBody.newPassword: New password must be at least 8 characters long," +
                " contain at least one special character, one uppercase letter, and one lowercase letter, " +
                "changePassword.changePasswordBody.oldPassword: Old password cannot be null";

        @SuppressWarnings("unchecked")
        List<ExceptionResponse> result = (List<ExceptionResponse>) parseMethod.invoke(globalExceptionHandler, errorString);

        // Kiểm tra số lượng lỗi trong danh sách
        assertEquals(2, result.size());

        // Kiểm tra lỗi đầu tiên
        ExceptionResponse firstError = result.get(0);
        assertEquals("newPassword", firstError.getField());
        assertEquals("New password must be at least 8 characters long, contain at least one special character, one uppercase letter, and one lowercase letter", firstError.getMessage());

        // Kiểm tra lỗi thứ hai
        ExceptionResponse secondError = result.get(1);
        assertEquals("oldPassword", secondError.getField());
        assertEquals("Old password cannot be null", secondError.getMessage());
    }

    @Test
    void testParseMessageToListExceptionResponse_PrivateMethod_SimpleMessage() throws Exception {
        // Sử dụng reflection để lấy phương thức private
        Method parseMethod = GlobalExceptionHandler.class.getDeclaredMethod("parseMessageToListExceptionResponse", String.class);
        parseMethod.setAccessible(true);

        String errorString = "changePassword.changePasswordBody.newPassword: New password must be at least 8 characters long," +
                " contain at least one special character, one uppercase letter, and one lowercase letter";

        @SuppressWarnings("unchecked")
        List<ExceptionResponse> result = (List<ExceptionResponse>) parseMethod.invoke(globalExceptionHandler, errorString);

        // Kiểm tra số lượng lỗi trong danh sách
        assertEquals(1, result.size());

        // Kiểm tra lỗi đầu tiên
        ExceptionResponse firstError = result.get(0);
        assertEquals("newPassword", firstError.getField());
        assertEquals("New password must be at least 8 characters long, contain at least one special character, one uppercase letter, and one lowercase letter", firstError.getMessage());

    }  @Test
    void testParseMessageToListExceptionResponse_PrivateMethod_EmptyMessage() throws Exception {
        // Sử dụng reflection để lấy phương thức private
        Method parseMethod = GlobalExceptionHandler.class.getDeclaredMethod("parseMessageToListExceptionResponse", String.class);
        parseMethod.setAccessible(true);

        String errorString = "";

        @SuppressWarnings("unchecked")
        List<ExceptionResponse> result = (List<ExceptionResponse>) parseMethod.invoke(globalExceptionHandler, errorString);

        // Kiểm tra số lượng lỗi trong danh sách
        assertEquals(0, result.size());


    }
}


