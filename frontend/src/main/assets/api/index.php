<?php
require_once 'flight/Flight.php';
require 'init.php';

Flight::map('handleSpaces', function($param){
	$ret1 = str_replace('%2520',' ', $param);
	$ret = str_replace('%20',' ', $ret1);
	return $ret;
});

//Qn 5a - Get Card IDs
Flight::route('GET /members', function(){
	Flight::queryArray(function($conn){
		return mysqli_prepare($conn, "select Card_ID from g1t06.card");
	});
});

//Qn 5b - Get Member details
Flight::route('GET /members/details/@card', function($card){
	Flight::queryRow(array("memberName","memberType","residencyType","replacements"),function($conn) use ($card){
		$q = "select c.`Name` as MemberName,  c.Member_Type AS MemberType,
 r.Remark AS ResidencyType,
NULLIF(count(c.Old_ID),0) as 'TotalNumberOfReplacedCards'
from  g1t06.card c
left join g1t06.residence_status r on r.ID = c.Residency_ID
where  `Name` = (
	select `Name`
	from g1t06.card
	where Card_ID = ?
)";
        $stmt = mysqli_prepare($conn, $q);
		mysqli_stmt_bind_param($stmt, 's', $card);
		return $stmt;
	});
});

//Qn 5c - Get Member transactions
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

//Qn 6 - Get Item Popularity
Flight::route('GET /item-popularity', function(){
	$start = Flight::request()->query->start;
	$end = Flight::request()->query['end'];
	Flight::queryTable(function($conn) use ($start, $end){
		$q="SELECT temp.item AS itemId, i.Title AS itemTitle, temp.n1, temp.n2, temp.score, (@cnt := @cnt + 1) AS rank
			FROM
			(select t1.item, t1.n1 as n1, t2.n2 as n2, (t1.n1+t2.n2) as score
			from (select it.Item_ID as item, count(*) as n1 from g1t06.transaction_item it, g1t06.transaction t
			where it.Card_ID=t.Card_ID
			and it.Tr_DateTime=t.Tr_DateTime
			and DATE(it.Tr_DateTime) between ? and ?
			and t.Transaction_Type= 'Lend'
			group by it.Item_ID)t1,
			(select it.Item_ID as item, count(*) as n2 from g1t06.transaction_item it, g1t06.transaction t
			where it.Card_ID=t.Card_ID
			and it.Tr_DateTime=t.Tr_DateTime
			and DATE(it.Tr_DateTime) between ? and ?
			and t.Transaction_Type= 'Renew'
			group by it.Item_ID)t2
			WHERE t1.item = t2.item
			order by score DESC
			limit 3) AS temp CROSS JOIN (SELECT @cnt := 0) AS temp2, item i
			WHERE i.Item_ID=temp.item";
		$stmt = mysqli_prepare($conn, $q);
		mysqli_stmt_bind_param($stmt, 'ssss', $start,$end,$start, $end);
		return $stmt;
	});
});

//Qn 7a - Get Item Types
Flight::route('GET /item-types', function(){
	Flight::queryArray(function($conn){
		return mysqli_prepare($conn, "select `Name` from g1t06.item_type");
	});
});

//Qn 7b - Get Item Details
Flight::route('GET /series/@kind', function($kind){
	Flight::queryTable(function ($conn) use ($kind){
		$q = "SELECT Series_Title AS series, Title AS title,  Series_Order AS `order`, count(distinct Lib_Name) AS numLibs FROM g1t06.item it, g1t06.library_copies lc
where Series_Title is not null
and it.Item_ID=lc.Item_ID
and it.Item_Type= ?
group by Series_Title, Series_Order, Title
order by Series_Title asc, Series_Order asc";
	$stmt = mysqli_prepare($conn, $q);
	mysqli_stmt_bind_param($stmt, "s", $kind);
	return $stmt;
	});
});

//Qn 8 - dropdown
Flight::route("GET /libraries", function(){
	Flight::queryArray(function($conn){
		return mysqli_prepare($conn, "select Lib_Name from g1t06.library");
	});
});

Flight::map('allSchools', function(){
	Flight::queryTable(function ($conn){
		$q = "select sch.Sch_Name as school, sch.Address as addr from
school sch where sch.Sch_Name not in (
select distinct Sch_Name from school_visit)
order by sch.Sch_Name asc";
	$stmt = mysqli_prepare($conn, $q);
	return $stmt;
	});
});

Flight::map('schoolsByLibrary', function($lib){
	Flight::queryTable(function ($conn) use ($lib){
		$q = "select sch.Sch_Name as school, sch.Address as addr from
school sch where sch.Sch_Name not in (
select distinct Sch_Name from school_visit where Lib_Name=?)
order by Sch_Name asc";
	$stmt = mysqli_prepare($conn, $q);
	mysqli_stmt_bind_param($stmt, "s", $lib);
	return $stmt;
	});
});

Flight::before('schoolsByLibrary', function(&$params, &$output){
	$params[0] = Flight::handleSpaces($params[0]);
});

//QN 8 - Schools Not Visited
Flight::route('GET /wishlist/schools', function(){
	$parameter = Flight::request()->query->lib;
	if($parameter==NULL){
		Flight::allSchools();
	} else {
		Flight::schoolsByLibrary(Flight::handleSpaces($parameter));
	}
});

Flight::map('allBooks', function(){
	Flight::queryTable(function ($conn){
		$q = "select cp.Lib_Name as library, it.Series_Title as series, it.Series_Order as `order`, it.Title as title, cp.Num_of_copies AS numCopies from
item it left outer join visit_items vi
on vi.Item_ID=it.Item_ID
inner join library_copies cp
where vi.Lib_Name is null and vi.Visit_date is null
and cp.Item_ID=it.Item_ID
and it.Item_Type= 'Book'
order by cp.Lib_Name Asc, Series_Title asc, Series_Order asc, Num_of_copies desc, it.Title asc";
	$stmt = mysqli_prepare($conn, $q);
	return $stmt;
	});
});

Flight::map('booksByLibrary', function($lib){
	Flight::queryTable(function ($conn) use ($lib){
		$q = "select cp.Lib_Name as library, it.Series_Title as series, it.Series_Order as `order`, it.Title as title, cp.Num_of_copies AS numCopies from
item it left outer join visit_items vi
on vi.Item_ID=it.Item_ID
inner join library_copies cp
where vi.Lib_Name is null and vi.Visit_date is null
and cp.Item_ID=it.Item_ID
and cp.Lib_Name=?
and it.Item_Type= 'Book'
order by cp.Lib_Name Asc, Series_Title asc, Series_Order asc, Num_of_copies desc, it.Title asc";
	$stmt = mysqli_prepare($conn, $q);
	mysqli_stmt_bind_param($stmt, "s", $lib);
	return $stmt;
	});
});


// Qn 8 - Books not yet brought
Flight::route('GET /wishlist/books', function(){
	$parameter = Flight::request()->query->lib;
	if($parameter==NULL){
		Flight::allBooks();
	} else {
		Flight::booksByLibrary(Flight::handleSpaces($parameter));
	}
});

Flight::map('notFound', function(){
    echo "Site is not working properly";
});

Flight::start();
?>
