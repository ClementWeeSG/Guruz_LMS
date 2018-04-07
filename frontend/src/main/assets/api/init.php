<?php
require_once 'flight/Flight.php';

Flight::set("conn", null);

Flight::map("openDB", function(){
	$conn = mysqli_connect('localhost','root','','g1t06');
	if (!$conn) {
			exit('Could not connect; '. mysqli_connect_error($conn));
		}
	Flight::set("conn", $conn);
});

Flight::map("closeDB", function(){
	mysqli_close(Flight::get("conn"));
});

Flight::map("queryRow", function($tags, $prepare){
	Flight::openDB();
	$stmt = $prepare(Flight::get("conn"));
	mysqli_stmt_execute($stmt);
	$meta = mysqli_stmt_result_metadata($stmt); 
    while ($field = $meta->fetch_field()) 
    { 
        $params[] = &$row[$field->name]; 
    } 

    call_user_func_array("mysqli_stmt_bind_result", $params); 

    if (mysqli_stmt_fetch($stmt)) {
		$c = array_combine($tags, array_values($row));
        $result[] = $c; 
    }
	Flight::closeDB();
	Flight::json($result);
});

Flight::map("queryTable", function($tags, $prepare){
	Flight::openDB();
	$result = array();
	$stmt = $prepare(Flight::get("conn"));
	mysqli_stmt_execute($stmt);
	$meta = mysqli_stmt_result_metadata($stmt); 
    while ($field = $meta->fetch_field()) 
    { 
        $params[] = &$row[$field->name]; 
    } 

    call_user_func_array("mysqli_stmt_bind_result", $params); 

    while (mysqli_stmt_fetch($stmt)) { 
        $c = array_combine($tags, array_values($row));
        $result[] = $c; 
    }
	Flight::closeDB();
	Flight::json($result);
});

Flight::map("queryArray", function($prepare){
	Flight::openDB();
	$params = array();
	$result = array();
	$stmt = $prepare(Flight::get("conn"));
	mysqli_stmt_execute($stmt);
	
	$meta = mysqli_stmt_result_metadata($stmt); 

    mysqli_stmt_bind_result($stmt, $item); 
	

    while (mysqli_stmt_fetch($stmt)) { 
        array_push($result, $item); 
    }
	Flight::closeDB();
	Flight::json($result);
});
?>