export interface RecursiveTestClass {
    recursive: RecursiveTestClass;
    recursiveArray: RecursiveTestClass[];
    returnThis(): RecursiveTestClass;
}


