<?php
require_once 'flight/Flight.php';
require 'init.php';

//  Flight::set("flight.base_url","/guruz_lms/api/");

Flight::route('/', function(){
    echo 'hello world!';
});


Flight::route('GET /members', function(){
	Flight::queryArray(function($conn){
		return mysqli_prepare($conn, "select Card_ID from g1t06.card");
	});
});

Flight::route('GET /members/details/@card', function($card){
	//TODO
});

Flight::route('GET /members/transactions/@card', function($card){
	Flight::queryTable(array("dateTime","type","library"), function($conn) use ($card){
		//$query = "select * from g1t06.transactions where Card_ID=?";
		$stmt = mysqli_prepare($conn, "select * from g1t06.transactions where Card_ID=?");
		print $card;
		mysqli_stmt_bind_param($stmt, 's', $card);
		return $stmt;
	});	
});

/**
Flight::route('GET /item-popularity', function(){
	$start = $_GET["start"];
	$end = $_GET["end"];
	//TODO
});

*/

Flight::route('GET /item-types', function(){
	Flight::queryArray(function($conn){
		return mysqli_prepare($conn, "select `Name` from g1t06.item_type");
	});
});

Flight::route("GET /test", function(){
	echo "Test is a success!";
});

Flight::map('notFound', function(){
    echo "Site is not working properly";
});


Flight::start();
?>