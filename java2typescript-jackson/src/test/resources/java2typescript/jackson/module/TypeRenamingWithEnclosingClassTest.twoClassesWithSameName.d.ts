export interface TypeRenamingWithEnclosingClassTest$TestClass {
    fieldOfInnerClass: string;
    other: TestClass;
    renamedWithAnnotation: ClassNameChangedWithAnnotation;
}

export interface TestClass {
    fieldOfPackageProtectedClass: string;
}

export interface ClassNameChangedWithAnnotation {
    field: string;
}