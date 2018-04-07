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
	Flight::queryRow(array("memberName","memberType","residencyType","replacements"),function($conn) use ($card){
		$q = "select c.Name AS 'Member Name', c.Member_Type AS 'Member Type', r.Remark AS 'Residency Type', NULLIF(count(c.Card_Id)-1,0) AS 'Total Number of Replaced Cards'".
" from g1t06.card c, g1t06.residence_status r"." where r.ID = c.Residency_ID and Card_ID=?";
        $stmt = mysqli_prepare($conn, $q);
		mysqli_stmt_bind_param($stmt, 's', $card);
		return $stmt;
	});
});

Flight::route('GET /members/transactions/@card', function($card){
	Flight::queryTable(function($conn) use ($card){
		$query = "SELECT DISTINCT
    t.Tr_DateTime as dateTime,
    t.Transaction_Type as kind,
    COALESCE(lc.Lend_Count, 0) AS numLent,
    COALESCE(rc.Renew_Count, 0) AS numRenewed,
    t.Fine_Amount as fine
FROM
    transaction t
        LEFT OUTER JOIN
    (SELECT
        t.Card_ID, t.Tr_DateTime, (COUNT(i.Item_ID)) AS 'Lend_Count'
    FROM
        transaction t
    LEFT OUTER JOIN transaction_item ti ON t.Card_ID = ti.Card_ID
        AND t.Tr_DateTime = ti.Tr_DateTime
    LEFT OUTER JOIN item i ON ti.Item_ID = i.Item_ID
    WHERE
        t.Transaction_Type = 'Lend'
            AND i.Item_Type = 'Book'
    GROUP BY t.Card_ID , t.Tr_DateTime) AS lc ON t.Card_ID = lc.Card_ID
        AND t.Tr_DateTime = lc.Tr_DateTime
        LEFT OUTER JOIN
    (SELECT
        t.Card_ID,
            t.Tr_DateTime,
            (COUNT(i.Item_ID)) AS 'Renew_Count'
    FROM
        transaction t
    LEFT OUTER JOIN transaction_item ti ON t.Card_ID = ti.Card_ID
        AND t.Tr_DateTime = ti.Tr_DateTime
    LEFT OUTER JOIN item i ON ti.Item_ID = i.Item_ID
    WHERE
        t.Transaction_Type = 'Renew'
            AND i.Item_Type = 'Book'
    GROUP BY t.Card_ID , t.Tr_DateTime) AS rc ON t.Card_ID = rc.Card_ID
        AND t.Tr_DateTime = rc.Tr_DateTime
        LEFT OUTER JOIN
    transaction_item ti ON t.Card_ID = ti.Card_ID
        AND t.Tr_DateTime = ti.Tr_DateTime
        LEFT OUTER JOIN
    item i ON ti.item_ID = i.Item_ID
 WHERE
    i.Item_Type = 'Book'
 and t.Card_ID=? ";
		$stmt = mysqli_prepare($conn, $query);
		//print $card;
		mysqli_stmt_bind_param($stmt, 's', $card);
		return $stmt;
	});
});

Flight::route('GET /item-popularity', function(){
	$start = $_GET["start"];
	$end = $_GET["end"];
	Flight::queryTable(function($conn) use ($start, $end){
		//TODO
	});
});

Flight::route('GET /item-types', function(){
	Flight::queryArray(function($conn){
		return mysqli_prepare($conn, "select `Name` from g1t06.item_type");
	});
});

Flight::route('GET /series/@item', function($item){
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