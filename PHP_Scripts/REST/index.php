<?php

    $requestUri = explode('/', $_SERVER['REQUEST_URI']);
    array_shift($requestUri); // �������� ����� ������ ������ ������� ������ ������ ''
       
    if (array_shift($requestUri) == 'api')
    {
        $apiName = array_shift($requestUri); 
        if ($apiName == "users")
        {
            include 'UserApi.php';
            $userApi = new UserApi($requestUri);
            $userApi->run();
        }
    }
?>