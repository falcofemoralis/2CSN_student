<?php

    //abstract class Api
    class Api
    {
        protected $method = ''; // ����� ������� (GET/POST/PUT/DELETE)

        public $requestUri = [];
        protected $requestParams = [];
        
        protected $action = ''; // �������� ������ ��� ��������
        
        //����������� "��������" �� ������� ��� ����������� ������ (��� �������, ��������� ���������� � URI, ��������� ���������� � ���� �������)
        public function __construct()
        {
            $this->method = $_SERVER['REQUEST_METHOD'];
            $this->requestUri = explode('/', $_SERVER['REQUEST_URI']);
            $this->requestParams = $_REQUEST;
            array_shift($this->requestUri); // �������� ����� ������ ������ ������� ������ ������ ''
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
        
        /*abstract protected function createAction();
        abstract protected function viewAction();
        abstract protected function deleteAction();
        abstract protected function updateAction();*/
    }

?>