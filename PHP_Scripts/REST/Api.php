<?php
    require_once 'DataBase.php';
    
    //abstract class Api
    abstract class Api
    {
        protected $method = ''; // ����� ������� (GET/POST/PUT/DELETE)

        protected $requestUri = [];
        
        protected $action = ''; // �������� ������ ��� ��������
        protected $connect;
        
        //����������� "��������" �� ������� ��� ����������� ������ (��� �������, ��������� ���������� � URI, ��������� ���������� � ���� �������)
        public function __construct($requestUri)
        {
            $db = new DataBase();
            $this->connect = $db->getConnection();
            $this->method = $_SERVER['REQUEST_METHOD'];
            $this->requestUri = $requestUri;
        }
          
        // ������������ ������
        public function run()
        {
            // ���������� ��������
            $this->action = $this->getAction();
            
            // ��������� �������������� �� ��� �������� � ����������
            if (method_exists($this, $this->action))
                return $this->{$this->action}();
            else 
                throw new RuntimeException('Invalid method', 405);
        }
           
        // ����� API ������� ����� ���������� � ����������� �� ���� �������
        public function getAction()
        {
            switch ($this->method)
            {
                case "POST":
                    return 'createAction';
                    break;
                case "GET":
                    return 'viewAction';
                    break;
                case "DELETE":
                    return 'deleteAction';
                    break;
                case "PUT":
                    return 'updateAction';
                    break;
            }
        }
        
        abstract protected function createAction();
        abstract protected function viewAction();
        abstract protected function deleteAction();
        abstract protected function updateAction();
    }

?>