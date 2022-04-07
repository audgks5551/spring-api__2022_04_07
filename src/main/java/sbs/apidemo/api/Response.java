package sbs.apidemo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private String error;
    private List<T> data;

    public static <T> Response<T> put(T data, String error) {
        List<T> list = new ArrayList<>();
        list.add(data);

        return new Response(null, list);
    }
}
