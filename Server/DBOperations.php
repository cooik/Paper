<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of DBOperations
 *
 * @author sony
 */
class DBOperations {

    private $host = '127.0.0.1';
    private $user = 'root';
    private $db = 'tan';
    private $pass = '';
    private $conn;

    public function __construct() {

        $this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db, $this->user, $this->pass);
    }

    public function checkLogin($work_id, $password) {
        $sql = 'SELECT * FROM users WHERE work_id = :work_id';
        $query = $this->conn->prepare($sql);
        $query->execute(array(':work_id' => $work_id));
        $data = $query->fetchObject();
        $db_password = $data->password;
        if ($db_password == $password) {
            $user["name"] = $data->name;
            $user["email"] = $data->email;
            $user["work_id"] = $data->work_id;
            $response["user"] = $user;
            $response["result"] = "success";
            $response["message"] = "Login Successful";
        } else {
            $response ["result"] = "failure";
            $response ["message"] = "Passwork is wrong !";
            echo mysql_error($this->conn);
        }
        echo json_encode($response);
    }

    public function searchPapers($key) {
        $search = "%$key%";
        $sql = 'SELECT COUNT(*) from papers WHERE title LIKE ?';
        $query = $this->conn->prepare($sql);
        $query->execute([$search]);
        $row_count = $query->fetchColumn();
        if ($row_count != 0) {
            $query = $this->conn->prepare("SELECT * FROM papers WHERE ( author LIKE :key OR title LIKE :key OR description LIKE :key)");
            $query->bindParam(':key', $search, PDO::PARAM_STR);
            $query->execute();
            $json_result = array();
            while ($tmp = $query->fetch()) {
                $json_result[] = $tmp;
            }
            return $json_result;
        }
    }
    
     public function searchPapersbyName($name) {;
        $sql = 'SELECT COUNT(*) from papers WHERE author = ?';
        $query = $this->conn->prepare($sql);
        $query->execute([$name]);        
        $row_count = $query->fetchColumn();
        if ($row_count != 0) {
            $query = $this->conn->prepare("SELECT * FROM papers WHERE author = :name");
            $query->bindParam(':name', $name, PDO::PARAM_STR);
            $query->execute();
            $json_result = array();
            while ($tmp = $query->fetch()) {
                $json_result[] = $tmp;
            }
            return $json_result;
        }
    }
    public function insertUserData($work_id, $name, $email, $password) {
        $sql = 'INSERT INTO users SET work_id =:work_id,name =:name,
    email =:email,password =:password,created_at = NOW()';

        $query = $this->conn->prepare($sql);
        $query->execute(array('work_id' => $work_id, ':name' => $name, ':email' => $email,
            ':password' => $password));

        if ($query) {

            return true;
        } else {
            return false;
        }
    }
    public function insertPaper($author, $title, $categories, $journal_title,$description,$file_name){
        $sql = 'INSERT INTO papers SET author =:author,title =:title,
    categories =:categories,journal_title =:journal_title,description =:description,file_name =:file_name,created_at = NOW()';
        $query = $this->conn->prepare($sql);
        $query->execute(array('author' => $author, ':title' => $title, ':categories' => $categories,
            ':journal_title' => $journal_title,':description'=>$description,':file_name'=>$file_name));
        if ($query) {
            return true;
        } else {
            return false;
        }
        
    }

    public function checkUserExist($work_id) {

        $sql = 'SELECT COUNT(*) from users WHERE work_id =:work_id';
        $query = $this->conn->prepare($sql);
        $query->execute(array('work_id' => $work_id));

        if ($query) {

            $row_count = $query->fetchColumn();

            if ($row_count == 0) {

                return false;
            } else {

                return true;
            }
        } else {

            return false;
        }
    }

    public function checkpws($work_id, $password) {
        $sql = 'SELECT * FROM users WHERE work_id = :work_id';
        $query = $this->conn->prepare($sql);
        $query->execute(array(':work_id' => $work_id));
        $data = $query->fetchObject();
        $db_password = $data->password;

        if ($db_password == $password) {
            return true;
        } else {
            return false;
        }
    }
   public function deletePaper($id){
        $sql = 'DELETE FROM papers WHERE id = :id';
        $query = $this->conn->prepare($sql);
        $query->execute(array(':id' => $id));
        if($query){
           $response["result"] = "success";
           $response["message"] = "Delete Successful";
        }else{
           $response ["result"] = "failure";
           $response ["message"] = "Passwork is wrong !";
           echo mysql_error($this->conn);
        }
        echo json_encode($response);
   }    

}
