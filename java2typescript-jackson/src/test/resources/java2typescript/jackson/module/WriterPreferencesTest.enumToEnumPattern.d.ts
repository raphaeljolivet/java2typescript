/** base class for implementing enums with Typesafe Enum Pattern (to be able to use enum names, instead of ordinal values, in a type-safe manner) */
export class EnumPatternBase {
    constructor(public name: string){}
    toString(){ return this.name; }
}

export class Enum extends EnumPatternBase {
    static VAL1 = new Enum('VAL1');
    static VAL2 = new Enum('VAL2');
    constructor(name:string){super(name)}
}

