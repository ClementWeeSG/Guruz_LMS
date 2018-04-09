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
	$result=array();
	$stmt = $prepare(Flight::get("conn"));
	mysqli_stmt_execute($stmt);
	$db_result = mysqli_stmt_get_result($stmt);
	$row = mysqli_fetch_assoc($db_result);
    if($row)
    {
        $result = array_combine($tags, array_values($row));
    }
	Flight::closeDB();
	Flight::json($result);
});

Flight::map("queryTable", function($prepare){
	Flight::openDB();
	$stmt = $prepare(Flight::get("conn"));
	mysqli_stmt_execute($stmt);
	$db_result = mysqli_stmt_get_result($stmt);
	$rows = mysqli_fetch_all($db_result, MYSQLI_ASSOC);
	Flight::closeDB();
	Flight::json($rows);
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