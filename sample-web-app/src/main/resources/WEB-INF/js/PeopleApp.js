var p = People;

var prs = p.peopleRestService;

p.rootUrl = "http://someurl/root/";

var personList = prs.getPeopleList(1);
var onePerson = prs.getPeople("rrr@eee.com");
