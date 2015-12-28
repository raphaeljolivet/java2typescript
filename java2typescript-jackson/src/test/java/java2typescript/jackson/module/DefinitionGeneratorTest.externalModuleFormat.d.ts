export interface StringClass {
    someField: string;
}

export enum ChangedEnumName {
    VAL1,
    VAL2,
    VAL3,
}

export interface TestClass {
    _String: string;
    _boolean: boolean;
    _Boolean: boolean;
    _int: number;
    _float: number;
    stringArray: string[];
    map: { [key: string ]: boolean;};
    recursive: TestClass;
    recursiveArray: TestClass[];
    stringArrayList: string[];
    booleanCollection: boolean[];
    _enum: ChangedEnumName;
    aMethod(param0: boolean, param1: string): string;
}

