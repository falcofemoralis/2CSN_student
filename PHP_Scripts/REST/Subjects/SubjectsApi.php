<?php

require_once 'DataBase.php';
require_once 'Api.php';
require_once 'Subjects/Subjects.php';

class SubjectsApi extends Api
{
    // Добавление в базу новых данных
    protected function createAction()
    {
        echo "invalid method";
    }
    
    // Обновление данных
    protected function updateAction()
    {
        echo "invalid method";
    }
    
    // Просмотр данных
    protected function viewAction()
    {
	if (!empty($this->requestUri)) 
            getSubjectsByCourse($this->connect);
        else 
            echo "invalid method"; 
    }
    
    // Удаление данных
    protected function deleteAction()
    {
        echo "invalid method";
    }
    
}

?>