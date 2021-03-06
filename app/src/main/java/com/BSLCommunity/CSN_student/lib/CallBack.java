package com.BSLCommunity.CSN_student.lib;

/** Интерфейс колбека
 * Методы:
 * T - обобщенный параметр, поскольку ответом с сервера могут быть разные данные
 * call(T) - метод должен вызываться в случае если запрос был успешно обработан сервером. В метод передается строка ответа с сервера.
 * fail(String) - метод должен вызываться в случае если сервер не смог обработать запрос с сервера. В метод передается строка с сообщением об ошибке
 * */
public interface CallBack<T> {
    void call(T response);
    void fail(String message);
}
