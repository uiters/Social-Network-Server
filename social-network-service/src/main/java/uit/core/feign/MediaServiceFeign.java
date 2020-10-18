package uit.core.feign;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uit.core.entity.Image;

import java.util.List;

@FeignClient(name = "media-service", configuration = {MediaServiceFeign.MultipartSupportConfig.class})
public interface MediaServiceFeign {
    @PostMapping(value = {"/upload/{postId}"}, consumes = {"multipart/form-data"})
    List<String> uploadMultipleFile(@RequestPart(value = "images") MultipartFile[] images,
                                     @PathVariable long postId);

    @GetMapping("/image/{postId}")
    List<Image> getPostImages(@PathVariable long postId);

//    @Configuration
    public class MultipartSupportConfig {
//        @Autowired
//        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
