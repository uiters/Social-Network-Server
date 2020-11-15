package uit.auth.feign;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@FeignClient(name = "media-service", configuration = {MediaServiceFeign.MultipartSupportConfig.class})
public interface MediaServiceFeign {
    @PostMapping(value = {"/upload/single"}, consumes = {"multipart/form-data"})
    String uploadSingleFile(@RequestPart(value = "file") MultipartFile file
    );


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
