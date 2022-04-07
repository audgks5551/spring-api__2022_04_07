package sbs.apidemo.apiv1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseV1<T> {
    private String error;
    private List<T> data;

    public static <T> ResponseV1<T> put(T data, String error) {
        List<T> list = new ArrayList<>();
        list.add(data);

        return new ResponseV1(null, list);
    }
}
