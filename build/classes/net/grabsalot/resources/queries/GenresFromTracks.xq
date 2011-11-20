for $genre in fn:distinct-values(//track/genre)
order by $genre
return
  <genre>{$genre}</genre>
