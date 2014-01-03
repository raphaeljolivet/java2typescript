declare module People {

export interface PeopleRestService {
    updatePerson(email: string,firstName: string,lastName: string):Person;
    deletePerson(email: string):any;
    addPerson(email: string,firstName: string,lastName: string):any;
    getPeople(email: string):Person;
}

export interface Person {
    email: string;
    firstName: string;
    lastName: string;
}

    export var peopleRestService: PeopleRestService;
    export var adapter : (a: string) => void;
    export var rootUrl: Object;
    
}
