package consumer.products.server.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * המחלקה שתשלח את השגיאה
 */
@ControllerAdvice
public class OrderAdvice {

    @ResponseBody // מזיק את התשובה ל body של ה response
    @ExceptionHandler(OrderNotFoundException.class) // יופעל אך ורק תיזרק שגיאה
    @ResponseStatus(HttpStatus.NOT_FOUND) // איזה status יישלח

    String orderNotFoundHandler(OrderNotFoundException one){
        return one.getMessage();

    }
}


