xquery version "1.0";
declare copy-namespaces no-preserve, inherit;

declare namespace stat = "http://www.grabsalot.org/stat";

declare variable $size as xs:integer := xs:integer(0);
declare variable $sizeSum as xs:integer := xs:integer(0);
declare variable $count as xs:integer := xs:integer(0);

declare function stat:calculateSize () as xs:integer* {
	for $track in //track
	let $size as xs:integer := xs:integer($track/filesize)
	let $count := $count + xs:integer(1)
	return $size
};

declare function stat:main () as element() {
	let $sizeSum as xs:integer := fn:sum(stat:calculateSize())
	return <stat>{$sizeSum} bytes in {$count} tracks</stat>
};

stat:main()