package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-14T17:26:10.419Z[GMT]")
public class ApiException extends Exception {
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }


    public int getCode() {
        return code;
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

    public static ApiException forbidden() {
        return new ApiException(403, "Forbidden");
    }
}
