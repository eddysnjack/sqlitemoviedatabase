# sqlitemoviedatabase
that's my half finished partly school & partly fun project. it's working but still not look like as i imagined it.
it's a netbeans project and written in java. first i used mysql as database system then i turned it to sqlite. mysql was too big for that program and more importantly it wasnt portable.


first tab:
  moviedatabase.java is the where app start with. if there is no database file it will ask user to create a new one(according to internal sql lines in the file, i am not so sure that this is the rght way but it's working). and after you run the program there will be a table on top for listing movies in database. u can click to add button to add a movie. u can fill the movie info manually or u can use 'imdb id'(word that start with tt and following with numbers in the link of movie's imdb page) of the movie to fetch movie info automatically(thanks to jsoup library!). few seconds of wait and voila!, all the empty places filled out. plus u have nice big poster of the movie. then u can save this.
when u click a row, program will show additional information that u have in your database(actors, directors, poster, imdb links and other stuff)

second tab:
  u can search by sql commands from searchfield or u can use various filters at boottom.

ps: i am aware of that there are a lots things need to be done. but thats how far i could came so far. and also there are lots of unneccessary files and lines. need a cleaning. dont have time unfortunately.
