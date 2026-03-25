package SmartAirAndHealthMointoring.demo.configuration;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {

    private String message;
    private boolean success;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ResponseDto<T> success(T data, String message) {
        return ResponseDto.<T>builder()
                .message(message)
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ResponseDto<T> error(String message) {
        return ResponseDto.<T>builder()
                .message(message)
                .success(false)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}