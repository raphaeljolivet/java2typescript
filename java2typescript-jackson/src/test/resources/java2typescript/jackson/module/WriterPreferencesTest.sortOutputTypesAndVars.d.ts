export interface A {
}

export class ConstantsStatic {
    static MY_CONSTANT_BOOLEAN: boolean = true;
    static MY_CONSTANT_STRING: string = 'stringValue';
}

export interface D {
}

export enum E {
    A,
    B,
    C,
}

export enum Enum {
    VAL1,
    VAL2,
}

export interface F {
    A: string;
    B: string;
    C: string;
    a(): void;
    b(): void;
    c(): void;
}

export interface TestClass {
    someEnum: Enum;
    someField: string;
}

export interface Z {
}

export var a: A;
export var z: Z;
