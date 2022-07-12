package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALREADY_EXISTS_USER_ID("error.already.exists.user.id");

    private final String message;
}
