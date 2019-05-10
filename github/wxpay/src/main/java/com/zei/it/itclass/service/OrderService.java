package com.zei.it.itclass.service;

import com.zei.it.itclass.domain.Order;

public interface OrderService {

    String save(Order order);

    Order getOne(int id);

    Order getByOrderNo(String orderNo);

    Integer updateOrderByOrderNo(Order order);
}
