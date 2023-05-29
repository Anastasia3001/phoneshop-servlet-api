package com.es.phoneshop.web;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.CartServiceImpl;
import com.es.phoneshop.service.impl.OrderServiceImpl;
import com.thoughtworks.qdox.model.expression.Or;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.stream.StreamFilter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;
    private static final String ORDER = "order";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String DELIVERY_ADDRESS = "deliveryAddress";
    private static final String DELIVERY_DATE = "deliveryDate";
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String PAYMENT_METHODS = "paymentMethods";
    private static final String MESSAGE = "Value is required";
    private static final String DATE_ERROR_MESSAGE = "Date entered incorrectly";
    private static final String FIRST_NAME_ERROR_MESSAGE = "First name must contain letters a-zA-Z";
    private static final String LAST_NAME_ERROR_MESSAGE = "Last name must contain letters a-zA-Z";
    private static final String PHONE_ERROR_MESSAGE = "Phone must match the template +375(29|44|25|33)###-##-##";
    private static final String ERRORS = "errors";
    private static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";

    @Override
    public void init() {
        cartService = CartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        request.setAttribute(ORDER, order);
        request.setAttribute(PAYMENT_METHODS, orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();
        setRequiredParameter(request, FIRST_NAME, errors, order::setFirstName);
        setRequiredParameter(request, LAST_NAME, errors, order::setLastName);
        setRequiredParameter(request, PHONE, errors, order::setPhone);
        setRequiredParameter(request, DELIVERY_ADDRESS, errors, order::setDeliveryAddress);
        setDeliveryMethod(request, errors, order);
        setPaymentMethod(request, errors, order);
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            response.sendRedirect(String.format("%s/overview/%d", request.getContextPath(), order.getId()));
        } else {
            request.setAttribute(ERRORS, errors);
            request.setAttribute(ORDER, order);
            request.setAttribute(PAYMENT_METHODS, orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, MESSAGE);
        } else if (parameter.equals(FIRST_NAME) && !value.matches("^[A-Za-z -]+$")) {
            errors.put(parameter, FIRST_NAME_ERROR_MESSAGE);
        } else if (parameter.equals(LAST_NAME) && !value.matches("^[A-Za-z -]+$")) {
            errors.put(parameter, LAST_NAME_ERROR_MESSAGE);
        } else if (parameter.equals(PHONE) && !value.matches("^\\+375\\((29|44|25|33)\\)[0-9]{3}-[0-9]{2}-[0-9]{2}$")) {
            errors.put(parameter, PHONE_ERROR_MESSAGE);
        } else {
            consumer.accept(value);
        }
    }

    private void setDeliveryMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(DELIVERY_DATE);
        if (value == null || value.isEmpty()) {
            errors.put(DELIVERY_DATE, MESSAGE);
        } else if (LocalDate.parse(value).isBefore(LocalDate.now())) {
            errors.put(DELIVERY_DATE, DATE_ERROR_MESSAGE);
        }
        else {
            order.setDeliveryDate(LocalDate.parse(value));
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(PAYMENT_METHOD);
        if (value.isEmpty()) {
            errors.put(PAYMENT_METHOD, MESSAGE);
        }
        else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
