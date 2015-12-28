/// <reference path="People.d.ts" />
import p = People;
import Person = p.Person;
import prs = p.peopleRestService;

p.rootUrl = "http://someurl/root/";

var personList : Person[] = prs.getPeopleList(1);
var onePerson : Person = prs.getPeople("rrr@eee.com");
