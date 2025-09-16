package bo.com.edu.diplomado.tuSaliud.Controller;
import bo.com.edu.diplomado.tuSaliud.Models.Request.ApiRequest;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
@Controller
@Slf4j
public class ApiController {
    public <T> ApiRequest<T> buildApiRequest(final Class<T> body, T data){
        ApiRequest<T> apiRequest = new ApiRequest<>();
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
        HttpServletRequest httpRequest = ((ServletRequestAttributes) attrs).getRequest();
        Map<String, List<String>> headersMap = Collections.list(httpRequest.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(httpRequest.getHeaders(h))
                ));
        Marker uuidMarker = MarkerFactory.getMarker(UUID.randomUUID().toString());
        apiRequest.setUuid(uuidMarker.getName());
//        apiRequest.setAppCode(SecurityContextHolder.getContext().getAuthentication().getName());
        apiRequest.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        apiRequest.setHttpMethod(httpRequest.getMethod());
        apiRequest.setRemoteHost(httpRequest.getRemoteHost());
        apiRequest.setHeaders(headersMap);
        apiRequest.setData(data);
//        log.info(uuidMarker, new Gson().toJson(apiRequest), uuidMarker.getName());
        return apiRequest;
    }
    public <T> ApiResponse<T> logApiResponse(ApiResponse<T> apiResponse) {
        if (apiResponse.getUuid() == null || apiResponse.getUuid().isEmpty()) {
            apiResponse.setUuid(UUID.randomUUID().toString());
        }
        Marker uuidMarker = MarkerFactory.getMarker(apiResponse.getUuid());
        if (apiResponse.getStatus() == 0 ||  apiResponse.getStatus() == HttpStatus.OK.value()) {
//            log.info(uuidMarker, new Gson().toJson(apiResponse), uuidMarker.getName());
        }else {
            //TODO: Improve logs, add info from request and duration
//            log.error(uuidMarker, new Gson().toJson(apiResponse), uuidMarker.getName());
        }
        return apiResponse;
    }
}
