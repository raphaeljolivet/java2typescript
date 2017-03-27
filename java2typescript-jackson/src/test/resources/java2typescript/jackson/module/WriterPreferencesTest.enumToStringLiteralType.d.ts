export type Enum =
    "VAL1"
    | "VAL2"
    | "name";

export interface TestClassHasMapWithEnumKey {
    enumKeyMap: { [key in Enum ] ? : string;};
}

export type EnumOneValue =
    "VAL1";
