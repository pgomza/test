package com.horeca.annotations;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST,
        RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.OPTIONS })
public @interface AllowCORS {
}
