package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")
public class ApiException extends Exception {
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public static ApiException unauthorized() {
        return new ApiException(401, "Unauthorized");
    }

    public static ApiException notFound() {
        return new ApiException(404, "Not found");
    }

    public static ApiException badRequest(String message) {
        return new ApiException(400, message);
    }
}
