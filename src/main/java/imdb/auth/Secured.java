package imdb.auth;

import java.lang.annotation.*;

import javax.ws.rs.*;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

}
